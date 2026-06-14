package com.daraz.web.dto.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : yashen
 * @created : 5/16/26
 * @project : web
 * @email : yashensavindu@gmail.com
 * @since : 0.1.0
 **/

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductSearchDTO {
    String searchText;
    Double minPrice;
    Double maxPrice;
    Integer numOfStars;
    String searingOrder;
    String sortBy;
}