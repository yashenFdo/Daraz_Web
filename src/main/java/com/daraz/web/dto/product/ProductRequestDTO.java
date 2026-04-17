package com.daraz.web.dto.product;
import com.daraz.web.entity.ProductCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


/**
 * @author : yashen
 * @created : 4/16/26
 * @project : web
 * @email : yashensavindu@gmail.com
 * @since : 0.1.0
 **/

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductRequestDTO {

    private String sku;
    private String productName;
    private String productDescription;
    private String brandName;
    private String warranty;
    private ProductCategory category;
    private int quantityOnHand;
    private String sizeOfOneUnit;
    private BigDecimal originalPrice;
    private BigDecimal discountPercentage;

    private String productImageUrlMain;
    private String productImageUrl1;
    private String productImageUrl2;
    private String productImageUrl3;

}