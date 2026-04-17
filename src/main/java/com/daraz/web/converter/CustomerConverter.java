package com.daraz.web.converter;

import com.daraz.web.dto.customer.CustomerDTO;
import com.daraz.web.entity.Customer;
import org.mapstruct.Mapper;

import java.util.List;


@Mapper(componentModel = "spring")
public interface CustomerConverter {

    Customer toEntity(CustomerDTO customerDTO);
    CustomerDTO toDto(Customer customer);
    List<CustomerDTO> toDtoList(List<Customer> customers);
}