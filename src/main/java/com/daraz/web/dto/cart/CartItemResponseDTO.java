package com.daraz.web.dto.cart;

import com.daraz.web.dto.product.ProductResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItemResponseDTO {
    private String itemId;
    private ProductResponseDTO product;
    private int quantity;
    private BigDecimal itemTotal;
}
