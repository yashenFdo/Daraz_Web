package com.daraz.web.service.impl;

import com.daraz.web.converter.CustomerConverter;
import com.daraz.web.dto.auth.AuthRequestDTO;
import com.daraz.web.dto.auth.AuthResponseDTO;
import com.daraz.web.dto.customer.CustomerDTO;
import com.daraz.web.entity.Customer;
import com.daraz.web.exception.custom.DuplicateEntryException;
import com.daraz.web.exception.custom.EntryNotFoundException;
import com.daraz.web.repo.CustomerRepo;
import com.daraz.web.security.JwtTokenProvider;
import com.daraz.web.service.AuthService;
import com.daraz.web.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final CustomerRepo customerRepo;
    private final CustomerConverter customerConverter;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;
    private final EmailService emailService;

    @Override
    @Transactional
    public CustomerDTO register(CustomerDTO customerDTO) {
        if (customerDTO.getPassword() == null || customerDTO.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }

        if (customerRepo.existsByEmail(customerDTO.getEmail())) {
            throw new DuplicateEntryException("Email is already registered!");
        }

        if (customerRepo.existsByNic(customerDTO.getNic())) {
            throw new DuplicateEntryException("NIC is already registered!");
        }

        if (customerRepo.existsByMobileNumber(customerDTO.getMobileNumber())) {
            throw new DuplicateEntryException("Mobile number is already registered!");
        }

        Customer customer = customerConverter.toEntity(customerDTO);
        customer.setPassword(passwordEncoder.encode(customerDTO.getPassword()));
        customer.setRole("USER");

        Customer savedCustomer = customerRepo.save(customer);
        CustomerDTO resultDto = customerConverter.toDto(savedCustomer);

        // Async welcome email trigger
        emailService.sendWelcomeEmail(resultDto.getEmail(), resultDto.getFirstName());

        return resultDto;
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponseDTO login(AuthRequestDTO dto) {
        Customer customer = customerRepo.findByEmail(dto.getEmail())
                .orElseThrow(() -> new EntryNotFoundException("Invalid email or password"));

        if (!customer.isActive()) {
            throw new IllegalArgumentException("Account is disabled");
        }

        if (customer.getPassword() == null || !passwordEncoder.matches(dto.getPassword(), customer.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(customer.getEmail());
        String token = jwtTokenProvider.generateToken(userDetails);

        return AuthResponseDTO.builder()
                .token(token)
                .customerId(customer.getId())
                .email(customer.getEmail())
                .role(customer.getRole())
                .build();
    }
}
