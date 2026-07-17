package com.daraz.web.converter;

import com.daraz.web.dto.cart.CartItemResponseDTO;
import com.daraz.web.dto.cart.CartResponseDTO;
import com.daraz.web.entity.Cart;
import com.daraz.web.entity.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;

@Mapper(componentModel = "spring", uses = {ProductConverter.class})
public interface CartConverter {

    @Mapping(target = "cartId", source = "id")
    @Mapping(target = "customerId", source = "customer.id")
    @Mapping(target = "totalAmount", expression = "java(calculateTotal(cart))")
    CartResponseDTO toDto(Cart cart);

    @Mapping(target = "itemId", source = "id")
    @Mapping(target = "itemTotal", expression = "java(calculateItemTotal(item))")
    CartItemResponseDTO toDto(CartItem item);

    default BigDecimal calculateItemTotal(CartItem item) {
        if (item == null || item.getProduct() == null || item.getProduct().getPriceAfterDiscount() == null) {
            return BigDecimal.ZERO;
        }
        return item.getProduct().getPriceAfterDiscount().multiply(BigDecimal.valueOf(item.getQuantity()));
    }

    default BigDecimal calculateTotal(Cart cart) {
        if (cart == null || cart.getItems() == null) {
            return BigDecimal.ZERO;
        }
        return cart.getItems().stream()
                .map(this::calculateItemTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
