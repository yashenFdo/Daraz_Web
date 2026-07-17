package com.daraz.web.dto.cart;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartResponseDTO {
    private String cartId;
    private String customerId;
    private List<CartItemResponseDTO> items;
    private BigDecimal totalAmount;
}
