package com.daraz.web.controller;

import com.daraz.web.dto.wishlist.WishlistResponseDTO;
import com.daraz.web.service.WishlistService;
import com.daraz.web.util.StandardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/wishlist")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;

    @GetMapping("/{customerId}")
    public ResponseEntity<StandardResponse> getWishlist(@PathVariable String customerId) {
        WishlistResponseDTO wishlist = wishlistService.getWishlist(customerId);
        return new ResponseEntity<>(
                new StandardResponse(
                        200,
                        "Fetched customer wishlist successfully",
                        wishlist
                ), HttpStatus.OK
        );
    }

    @PostMapping("/{customerId}/add/{productId}")
    public ResponseEntity<StandardResponse> addProductToWishlist(
            @PathVariable String customerId,
            @PathVariable String productId
    ) {
        WishlistResponseDTO wishlist = wishlistService.addProduct(customerId, productId);
        return new ResponseEntity<>(
                new StandardResponse(
                        200,
                        "Product added to wishlist successfully",
                        wishlist
                ), HttpStatus.OK
        );
    }

    @DeleteMapping("/{customerId}/remove/{productId}")
    public ResponseEntity<StandardResponse> removeProductFromWishlist(
            @PathVariable String customerId,
            @PathVariable String productId
    ) {
        WishlistResponseDTO wishlist = wishlistService.removeProduct(customerId, productId);
        return new ResponseEntity<>(
                new StandardResponse(
                        200,
                        "Product removed from wishlist successfully",
                        wishlist
                ), HttpStatus.OK
        );
    }
}
