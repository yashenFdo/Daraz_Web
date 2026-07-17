package com.daraz.web.repo;

import com.daraz.web.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepo extends JpaRepository<Cart, String> {
    Optional<Cart> findByCustomerId(String customerId);
}
