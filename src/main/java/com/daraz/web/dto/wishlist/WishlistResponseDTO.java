package com.daraz.web.dto.wishlist;

import com.daraz.web.dto.product.ProductResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WishlistResponseDTO {
    private String wishlistId;
    private String customerId;
    private List<ProductResponseDTO> products;
}
