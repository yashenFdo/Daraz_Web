package com.daraz.web.service;

import jakarta.persistence.Id;

public interface CustomerService <T,ID>{

    void saveCustomer(T customerDTO);
    void modifyCustomer(Id id, T customerDTO);
    void removeCustomer(ID id);
    void viewCustomer(Id id);


}