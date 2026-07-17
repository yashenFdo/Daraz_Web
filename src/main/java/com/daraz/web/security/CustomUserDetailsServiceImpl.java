package com.daraz.web.security;

import com.daraz.web.entity.Customer;
import com.daraz.web.repo.CustomerRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsServiceImpl implements UserDetailsService {

    private final CustomerRepo customerRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Customer customer = customerRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        String password = customer.getPassword() != null ? customer.getPassword() : "";

        return new User(
                customer.getEmail(),
                password,
                customer.isActive(),
                true, // accountNonExpired
                true, // credentialsNonExpired
                true, // accountNonLocked
                Collections.singletonList(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_" + customer.getRole()))
        );
    }
}
