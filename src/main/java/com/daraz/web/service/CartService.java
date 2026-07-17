package com.daraz.web.service;

import com.daraz.web.dto.cart.CartItemRequestDTO;
import com.daraz.web.dto.cart.CartResponseDTO;

public interface CartService {
    CartResponseDTO getCart(String customerId);
    CartResponseDTO addItem(String customerId, CartItemRequestDTO dto);
    CartResponseDTO updateQuantity(String customerId, String productId, int quantity);
    CartResponseDTO removeItem(String customerId, String productId);
    CartResponseDTO clearCart(String customerId);
}
