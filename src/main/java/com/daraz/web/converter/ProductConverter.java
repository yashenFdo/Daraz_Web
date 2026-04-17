package com.daraz.web.converter;

import com.daraz.web.dto.product.ProductRequestDTO;
import com.daraz.web.dto.product.ProductResponseDTO;
import com.daraz.web.entity.Product;
import org.mapstruct.Mapper;

/**
 * @author : yashen
 * @created : 4/17/26
 * @project : web
 * @email : yashensavindu@gmail.com
 * @since : 0.1.0
 **/
@Mapper(componentModel = "spring", uses = {})
public interface ProductConverter {

    Product toEntity(ProductRequestDTO productRequestDTO);
    ProductResponseDTO toDto(Product product);
}