package com.daraz.web.repo;

import com.daraz.web.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CustomerRepo extends JpaRepository<Customer,String> {

    //verification-part in account creation : check duplication
    boolean existsByNic(String nic);
    boolean existsByEmail(String email);
    boolean existsByMobileNumber(String mobileNumber);

    //verification-part in update : check duplication
    boolean existsByEmailAndIdNot(String email, String id);
    boolean existsByNicAndIdNot(String nic, String id);
    boolean existsByMobileNumberAndIdNot(String mobileNumber, String id);

    Object removeById(String id);
}
