package com.daraz.web.converter;

import com.daraz.web.dto.review.ProductReviewRequestDTO;
import com.daraz.web.dto.review.ProductReviewResponseDTO;
import com.daraz.web.entity.ProductReview;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductReviewConverter {

    @Mapping(target = "reviewId", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "date", ignore = true)
    ProductReview toEntity(ProductReviewRequestDTO dto);

    @Mapping(target = "customerName", expression = "java(review.getCustomer() != null ? review.getCustomer().getFirstName() + \" \" + review.getCustomer().getLastName() : null)")
    @Mapping(target = "productName", source = "product.productName")
    ProductReviewResponseDTO toDto(ProductReview review);

    List<ProductReviewResponseDTO> toDtoList(List<ProductReview> reviews);
}
