package com.daraz.web.service;


import java.util.List;

public interface CustomerService <T,ID>{

    T saveCustomer(T customerDTO);
    T modifyCustomer(ID id, T customerDTO);
    boolean removeCustomer(ID id);
    T viewCustomer(ID id);
    List<T> viewAllCustomers();


}