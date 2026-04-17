package com.daraz.web.converter;

import com.daraz.web.dto.category.ProductCategoryRequestDTO;
import com.daraz.web.dto.category.ProductCategoryResponseDTO;
import com.daraz.web.entity.ProductCategory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductCategoryConverter {

    ProductCategory toEntity(ProductCategoryRequestDTO productCategoryRequestDTO);
    ProductCategoryResponseDTO toDto(ProductCategory productCategory);
}