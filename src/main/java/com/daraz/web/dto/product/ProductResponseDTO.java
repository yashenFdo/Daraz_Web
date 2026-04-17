package com.daraz.web.dto.product;

import com.daraz.web.entity.ProductCategory;
import lombok.*;
import java.math.BigDecimal;

/**
 * @author : yashen
 * @created : 4/16/26
 * @project : web
 * @email : yashensavindu@gmail.com
 * @since : 0.1.0
 **/

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseDTO {

    private String productId;
    private String sku;
    private String productName;
    private String brandName;
    private String shopName;
    private ProductCategory category;
    private String warranty;
    private int itemsSold;
    private BigDecimal originalPrice;
    private BigDecimal priceAfterDiscount;
    private BigDecimal discountPercentage;

    private String imageUrlMain;

    // UI
    private double averageRating;
    private int reviewCount;
    private boolean isAvailable;

    // In-stokes or Out of stokes
    private String stockStatus;
}