package com.daraz.web.repo;

import com.daraz.web.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WishlistRepo extends JpaRepository<Wishlist, String> {
    Optional<Wishlist> findByCustomerId(String customerId);
}
