package com.daraz.web.service;

import com.daraz.web.dto.wishlist.WishlistResponseDTO;

public interface WishlistService {
    WishlistResponseDTO getWishlist(String customerId);
    WishlistResponseDTO addProduct(String customerId, String productId);
    WishlistResponseDTO removeProduct(String customerId, String productId);
}
