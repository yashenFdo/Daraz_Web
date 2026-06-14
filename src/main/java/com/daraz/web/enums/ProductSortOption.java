package com.daraz.web.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ProductSortOption {
    PRICE_ASC("priceAfterDiscount", "asc"),
    PRICE_DESC("priceAfterDiscount", "desc"),
    RATING("averageRating", "desc"), // High ratings first
    NAME("productName", "asc"),
    NEWEST("productId", "desc");     // Newest items first

    private final String fieldName;
    private final String direction;

}