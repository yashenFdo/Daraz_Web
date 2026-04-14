package com.daraz.web.repo;

import com.daraz.web.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CustomerRepo extends JpaRepository<Customer,String> {

    boolean existsByNic(String nic);
}