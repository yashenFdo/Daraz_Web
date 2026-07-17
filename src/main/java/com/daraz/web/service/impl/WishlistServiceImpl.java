package com.daraz.web.service.impl;

import com.daraz.web.converter.WishlistConverter;
import com.daraz.web.dto.wishlist.WishlistResponseDTO;
import com.daraz.web.entity.Customer;
import com.daraz.web.entity.Product;
import com.daraz.web.entity.Wishlist;
import com.daraz.web.exception.custom.EntryNotFoundException;
import com.daraz.web.repo.CustomerRepo;
import com.daraz.web.repo.ProductRepo;
import com.daraz.web.repo.WishlistRepo;
import com.daraz.web.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepo wishlistRepo;
    private final CustomerRepo customerRepo;
    private final ProductRepo productRepo;
    private final WishlistConverter wishlistConverter;

    private Wishlist getOrCreateWishlist(String customerId) {
        return wishlistRepo.findByCustomerId(customerId)
                .orElseGet(() -> {
                    Customer customer = customerRepo.findById(customerId)
                            .orElseThrow(() -> new EntryNotFoundException("Customer not found with id: " + customerId));
                    Wishlist wishlist = new Wishlist();
                    wishlist.setCustomer(customer);
                    wishlist.setProducts(new ArrayList<>());
                    return wishlistRepo.save(wishlist);
                });
    }

    @Override
    @Transactional
    public WishlistResponseDTO getWishlist(String customerId) {
        Wishlist wishlist = getOrCreateWishlist(customerId);
        return wishlistConverter.toDto(wishlist);
    }

    @Override
    @Transactional
    public WishlistResponseDTO addProduct(String customerId, String productId) {
        Wishlist wishlist = getOrCreateWishlist(customerId);
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new EntryNotFoundException("Product not found with id: " + productId));

        if (!wishlist.getProducts().contains(product)) {
            wishlist.getProducts().add(product);
            wishlist = wishlistRepo.save(wishlist);
        }

        return wishlistConverter.toDto(wishlist);
    }

    @Override
    @Transactional
    public WishlistResponseDTO removeProduct(String customerId, String productId) {
        Wishlist wishlist = getOrCreateWishlist(customerId);
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new EntryNotFoundException("Product not found with id: " + productId));

        if (wishlist.getProducts().contains(product)) {
            wishlist.getProducts().remove(product);
            wishlist = wishlistRepo.save(wishlist);
        }

        return wishlistConverter.toDto(wishlist);
    }
}
