package com.daraz.web.service;

import com.daraz.web.dto.review.ProductReviewRequestDTO;
import com.daraz.web.dto.review.ProductReviewResponseDTO;

import java.util.List;

public interface ProductReviewService {
    ProductReviewResponseDTO save(ProductReviewRequestDTO dto);
    List<ProductReviewResponseDTO> getReviewsByProductId(String productId);
}
