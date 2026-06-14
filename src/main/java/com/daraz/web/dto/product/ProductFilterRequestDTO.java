package com.daraz.web.dto.product;

import com.daraz.web.enums.ProductSortOption;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : yashen
 * @created : 6/10/26
 * @project : web
 * @email : yashensavindu@gmail.com
 * @since : 0.1.0
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductFilterRequestDTO {
    private String searchText;
    private Double minPrice;
    private Double maxPrice;
    private Integer ratingStars;
    private ProductSortOption sortBy;
}