package com.daraz.web.converter;

import com.daraz.web.dto.category.ProductCategoryRequestDTO;
import com.daraz.web.dto.category.ProductCategoryResponseDTO;
import com.daraz.web.entity.ProductCategory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductCategoryConverter {

    @org.mapstruct.Mapping(target = "id", ignore = true)
    @org.mapstruct.Mapping(target = "parentCategory", expression = "java(mapParentCategory(dto.getParentId()))")
    @org.mapstruct.Mapping(target = "subCategories", ignore = true)
    @org.mapstruct.Mapping(target = "products", ignore = true)
    ProductCategory toEntity(ProductCategoryRequestDTO dto);

    @org.mapstruct.Mapping(target = "subCategoryNames", source = "subCategories")
    ProductCategoryResponseDTO toDto(ProductCategory productCategory);

    default ProductCategory mapParentCategory(Long parentId) {
        if (parentId == null) {
            return null;
        }
        ProductCategory parent = new ProductCategory();
        parent.setId(parentId);
        return parent;
    }

    default String mapSubCategoryToString(ProductCategory subCategory) {
        return subCategory != null ? subCategory.getName() : null;
    }
}