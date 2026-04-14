package com.daraz.web.service.impl;

import com.daraz.web.converter.CustomerConverter;
import com.daraz.web.dto.CustomerDTO;
import com.daraz.web.entity.Customer;
import com.daraz.web.exception.custom.DuplicateEntryException;
import com.daraz.web.exception.custom.EntryNotFoundException;
import com.daraz.web.repo.CustomerRepo;
import com.daraz.web.service.CustomerService;
import jakarta.persistence.Id;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author : yashen
 * @created : 4/13/26
 * @project : web
 * @email : yashensavindu@gmail.com
 * @since : 0.1.0
 **/

@RequiredArgsConstructor
@Service
public class CustomerServiceImpl implements CustomerService<CustomerDTO,String> {

    private final CustomerRepo customerRepo;
    private final CustomerConverter customerConverter;

    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        if(customerRepo.existsByNic(customerDTO.getNic())){
            throw new DuplicateEntryException("Duplicate Entry "+customerDTO.getNic()+ " is already exists!");
        }
        Customer customer = customerConverter.toEntity(customerDTO);
        Customer savedCustomer = customerRepo.save(customer);
        return customerConverter.toDto(savedCustomer);
    }

    @Override
    public CustomerDTO modifyCustomer(String id, CustomerDTO customerDTO) {
        return null;
    }

    @Override
    public boolean removeCustomer(String s) {
        return false;
    }

    @Override
    public CustomerDTO viewCustomer(String id) {
        return customerRepo.findById(id)
                .map(customer -> customerConverter.toDto(customer))
                .orElseThrow(()-> new EntryNotFoundException("Customer Account Not Found!, given account id : "+id));
    }

    @Override
    public List<CustomerDTO> viewAllCustomers() {
        return null;
    }
}