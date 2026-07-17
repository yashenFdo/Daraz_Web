package com.daraz.web.service.impl;

import com.daraz.web.converter.CartConverter;
import com.daraz.web.dto.cart.CartItemRequestDTO;
import com.daraz.web.dto.cart.CartResponseDTO;
import com.daraz.web.entity.Cart;
import com.daraz.web.entity.CartItem;
import com.daraz.web.entity.Customer;
import com.daraz.web.entity.Product;
import com.daraz.web.exception.custom.EntryNotFoundException;
import com.daraz.web.repo.CartItemRepo;
import com.daraz.web.repo.CartRepo;
import com.daraz.web.repo.CustomerRepo;
import com.daraz.web.repo.ProductRepo;
import com.daraz.web.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepo cartRepo;
    private final CartItemRepo cartItemRepo;
    private final CustomerRepo customerRepo;
    private final ProductRepo productRepo;
    private final CartConverter cartConverter;

    private Cart getOrCreateCart(String customerId) {
        return cartRepo.findByCustomerId(customerId)
                .orElseGet(() -> {
                    Customer customer = customerRepo.findById(customerId)
                            .orElseThrow(() -> new EntryNotFoundException("Customer not found with id: " + customerId));
                    Cart cart = new Cart();
                    cart.setCustomer(customer);
                    cart.setItems(new ArrayList<>());
                    return cartRepo.save(cart);
                });
    }

    @Override
    @Transactional
    public CartResponseDTO getCart(String customerId) {
        Cart cart = getOrCreateCart(customerId);
        return cartConverter.toDto(cart);
    }

    @Override
    @Transactional
    public CartResponseDTO addItem(String customerId, CartItemRequestDTO dto) {
        if (dto.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }

        Cart cart = getOrCreateCart(customerId);
        Product product = productRepo.findById(dto.getProductId())
                .orElseThrow(() -> new EntryNotFoundException("Product not found with id: " + dto.getProductId()));

        Optional<CartItem> existingItemOpt = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst();

        if (existingItemOpt.isPresent()) {
            CartItem existingItem = existingItemOpt.get();
            int newQuantity = existingItem.getQuantity() + dto.getQuantity();
            if (newQuantity > product.getQuantityOnHand()) {
                throw new IllegalArgumentException("Requested quantity exceeds available stock (" + product.getQuantityOnHand() + ")");
            }
            existingItem.setQuantity(newQuantity);
        } else {
            if (dto.getQuantity() > product.getQuantityOnHand()) {
                throw new IllegalArgumentException("Requested quantity exceeds available stock (" + product.getQuantityOnHand() + ")");
            }
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(dto.getQuantity());
            cart.getItems().add(newItem);
        }

        Cart savedCart = cartRepo.save(cart);
        return cartConverter.toDto(savedCart);
    }

    @Override
    @Transactional
    public CartResponseDTO updateQuantity(String customerId, String productId, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }

        Cart cart = getOrCreateCart(customerId);
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new EntryNotFoundException("Product not found with id: " + productId));

        CartItem item = cart.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(product.getId()))
                .findFirst()
                .orElseThrow(() -> new EntryNotFoundException("Product not in cart"));

        if (quantity > product.getQuantityOnHand()) {
            throw new IllegalArgumentException("Requested quantity exceeds available stock (" + product.getQuantityOnHand() + ")");
        }

        item.setQuantity(quantity);
        Cart savedCart = cartRepo.save(cart);
        return cartConverter.toDto(savedCart);
    }

    @Override
    @Transactional
    public CartResponseDTO removeItem(String customerId, String productId) {
        Cart cart = getOrCreateCart(customerId);
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new EntryNotFoundException("Product not found with id: " + productId));

        cart.getItems().removeIf(item -> item.getProduct().getId().equals(product.getId()));
        Cart savedCart = cartRepo.save(cart);
        return cartConverter.toDto(savedCart);
    }

    @Override
    @Transactional
    public CartResponseDTO clearCart(String customerId) {
        Cart cart = getOrCreateCart(customerId);
        cart.getItems().clear();
        Cart savedCart = cartRepo.save(cart);
        return cartConverter.toDto(savedCart);
    }
}
