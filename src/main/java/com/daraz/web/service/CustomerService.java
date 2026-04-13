package com.daraz.web.service;

import jakarta.persistence.Id;

import java.util.List;

public interface CustomerService <T,ID>{

    T saveCustomer(T customerDTO);
    T modifyCustomer(Id id, T customerDTO);
    boolean removeCustomer(ID id);
    T viewCustomer(Id id);
    List<T> viewAllCustomers();


}