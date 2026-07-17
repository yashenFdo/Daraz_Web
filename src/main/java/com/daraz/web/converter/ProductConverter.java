package com.daraz.web.converter;

import com.daraz.web.dto.product.ProductRequestDTO;
import com.daraz.web.dto.product.ProductResponseDTO;
import com.daraz.web.entity.Product;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @author : yashen
 * @created : 4/17/26
 * @project : web
 * @email : yashensavindu@gmail.com
 * @since : 0.1.0
 **/
@Mapper(componentModel = "spring", uses = {ProductCategoryConverter.class})
public interface ProductConverter {

    @org.mapstruct.Mapping(target = "id", ignore = true)
    @org.mapstruct.Mapping(target = "seller", ignore = true)
    @org.mapstruct.Mapping(target = "shopName", ignore = true)
    @org.mapstruct.Mapping(target = "itemsSold", ignore = true)
    @org.mapstruct.Mapping(target = "priceAfterDiscount", ignore = true)
    @org.mapstruct.Mapping(target = "averageRating", ignore = true)
    @org.mapstruct.Mapping(target = "reviewCount", ignore = true)
    @org.mapstruct.Mapping(target = "active", ignore = true)
    @org.mapstruct.Mapping(target = "deleted", ignore = true)
    @org.mapstruct.Mapping(target = "reviews", ignore = true)
    Product toEntity(ProductRequestDTO productRequestDTO);

    @org.mapstruct.Mapping(target = "productId", source = "id")
    @org.mapstruct.Mapping(target = "imageUrlMain", source = "productImageUrlMain")
    @org.mapstruct.Mapping(target = "isAvailable", expression = "java(product.isAvailable())")
    @org.mapstruct.Mapping(target = "stockStatus", expression = "java(product.getQuantityOnHand() > 0 ? \"In Stock\" : \"Out of Stock\")")
    ProductResponseDTO toDto(Product product);

    List<ProductResponseDTO> toResponseDTOList(List<Product> content);
}