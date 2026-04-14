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
import org.aspectj.weaver.ast.Not;
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
        Customer savedCustomer = customerRepo.save(customer);
        return customerConverter.toDto(savedCustomer);
    }

    @Override
    public CustomerDTO modifyCustomer(String id, CustomerDTO customerDTO) {
        // check the mobile and nic,email already used or not.
        // check customer by id
        // then save it.

        if(customerRepo.existsByNicAndIdNot(customerDTO.getNic(), customerDTO.getId())){
            throw new DuplicateEntryException("NIC already registered to another account!");
        }

        if(customerRepo.existsByEmailAndIdNot(customerDTO.getEmail(), customerDTO.getId())){
            throw new DuplicateEntryException("This email already registered to another account!");
        }

        if(customerRepo.existsByMobileNumberAndIdNot(customerDTO.getMobileNumber(), customerDTO.getId())){
            throw new DuplicateEntryException("Mobile-Number already registered to another account!");
        }

        return customerRepo.findById(id)
                .map(customer -> {
                    customer.setFirstName(customerDTO.getFirstName());
                    customer.setLastName(customerDTO.getLastName());
                    customer.setNic(customerDTO.getNic());
                    customer.setEmail(customerDTO.getEmail());
                    customer.setMobileNumber(customerDTO.getMobileNumber());

                    Customer updatedCustomer = customerRepo.save(customer);
                    return customerConverter.toDto(updatedCustomer);

                })
                .orElseThrow(()-> new EntryNotFoundException("Customer Account Not Found!, given account id : "+id));

    }

    @Override
    public boolean removeCustomer(String id) {
        if (!customerRepo.existsById(id)){
            throw new EntryNotFoundException("Customer Account Not Found!, given account id : "+id);
        }

        customerRepo.deleteById(id);
        return true; // so the thing is Spring data JPA's deleteById is void return type. we can get affected count of rows by define custom method in repo interface.
    }

    @Override
    public CustomerDTO viewCustomer(String id) {
        return customerRepo.findById(id)
                .map(customer -> customerConverter.toDto(customer))
                .orElseThrow(()-> new EntryNotFoundException("Customer Account Not Found!, given account id : "+id));
    }

    @Override
    public List<CustomerDTO> viewAllCustomers() {
        List<Customer> all = customerRepo.findAll();
        return customerConverter.toDtoList(all);
    }
}