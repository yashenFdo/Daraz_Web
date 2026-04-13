package com.daraz.web.service.impl;

import com.daraz.web.dto.CustomerDTO;
import com.daraz.web.service.CustomerService;
import jakarta.persistence.Id;

import java.util.List;

/**
 * @author : yashen
 * @created : 4/13/26
 * @project : web
 * @email : yashensavindu@gmail.com
 * @since : 0.1.0
 **/
public class CustomerServiceImpl implements CustomerService<CustomerDTO,String> {

    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        return null;
    }

    @Override
    public CustomerDTO modifyCustomer(Id id, CustomerDTO customerDTO) {
        return null;
    }

    @Override
    public boolean removeCustomer(String s) {
        return false;
    }

    @Override
    public CustomerDTO viewCustomer(Id id) {
        return null;
    }

    @Override
    public List<CustomerDTO> viewAllCustomers() {
        return List.of();
    }
}