package com.daraz.web.controller;

import com.daraz.web.dto.cart.CartItemRequestDTO;
import com.daraz.web.dto.cart.CartResponseDTO;
import com.daraz.web.service.CartService;
import com.daraz.web.util.StandardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping("/{customerId}")
    public ResponseEntity<StandardResponse> getCart(@PathVariable String customerId) {
        CartResponseDTO cart = cartService.getCart(customerId);
        return new ResponseEntity<>(
                new StandardResponse(
                        200,
                        "Fetched customer cart successfully",
                        cart
                ), HttpStatus.OK
        );
    }

    @PostMapping("/{customerId}/add")
    public ResponseEntity<StandardResponse> addItemToCart(
            @PathVariable String customerId,
            @RequestBody CartItemRequestDTO dto
    ) {
        CartResponseDTO cart = cartService.addItem(customerId, dto);
        return new ResponseEntity<>(
                new StandardResponse(
                        200,
                        "Item added to cart successfully",
                        cart
                ), HttpStatus.OK
        );
    }

    @PutMapping("/{customerId}/update/{productId}")
    public ResponseEntity<StandardResponse> updateItemQuantity(
            @PathVariable String customerId,
            @PathVariable String productId,
            @RequestParam int quantity
    ) {
        CartResponseDTO cart = cartService.updateQuantity(customerId, productId, quantity);
        return new ResponseEntity<>(
                new StandardResponse(
                        200,
                        "Cart item quantity updated successfully",
                        cart
                ), HttpStatus.OK
        );
    }

    @DeleteMapping("/{customerId}/remove/{productId}")
    public ResponseEntity<StandardResponse> removeItemFromCart(
            @PathVariable String customerId,
            @PathVariable String productId
    ) {
        CartResponseDTO cart = cartService.removeItem(customerId, productId);
        return new ResponseEntity<>(
                new StandardResponse(
                        200,
                        "Item removed from cart successfully",
                        cart
                ), HttpStatus.OK
        );
    }

    @DeleteMapping("/{customerId}/clear")
    public ResponseEntity<StandardResponse> clearCart(@PathVariable String customerId) {
        CartResponseDTO cart = cartService.clearCart(customerId);
        return new ResponseEntity<>(
                new StandardResponse(
                        200,
                        "Cart cleared successfully",
                        cart
                ), HttpStatus.OK
        );
    }
}
