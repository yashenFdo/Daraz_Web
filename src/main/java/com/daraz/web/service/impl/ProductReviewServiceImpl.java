package com.daraz.web.service.impl;

import com.daraz.web.converter.ProductReviewConverter;
import com.daraz.web.dto.review.ProductReviewRequestDTO;
import com.daraz.web.dto.review.ProductReviewResponseDTO;
import com.daraz.web.entity.Customer;
import com.daraz.web.entity.Product;
import com.daraz.web.entity.ProductReview;
import com.daraz.web.exception.custom.EntryNotFoundException;
import com.daraz.web.repo.CustomerRepo;
import com.daraz.web.repo.ProductRepo;
import com.daraz.web.repo.ProductReviewRepo;
import com.daraz.web.service.ProductReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductReviewServiceImpl implements ProductReviewService {

    private final ProductReviewRepo productReviewRepo;
    private final CustomerRepo customerRepo;
    private final ProductRepo productRepo;
    private final ProductReviewConverter productReviewConverter;

    @Override
    @Transactional
    public ProductReviewResponseDTO save(ProductReviewRequestDTO dto) {
        Customer customer = customerRepo.findById(dto.getCustomerId())
                .orElseThrow(() -> new EntryNotFoundException("Customer not found with id: " + dto.getCustomerId()));

        Product product = productRepo.findById(dto.getProductId())
                .orElseThrow(() -> new EntryNotFoundException("Product not found with id: " + dto.getProductId()));

        ProductReview review = productReviewConverter.toEntity(dto);
        review.setCustomer(customer);
        review.setProduct(product);

        ProductReview savedReview = productReviewRepo.save(review);

        // Update the running average rating and review count of the product
        product.updateRating(dto.getNumOfStars());
        productRepo.save(product);

        return productReviewConverter.toDto(savedReview);
    }

    @Override
    public List<ProductReviewResponseDTO> getReviewsByProductId(String productId) {
        if (!productRepo.existsById(productId)) {
            throw new EntryNotFoundException("Product not found with id: " + productId);
        }
        List<ProductReview> reviews = productReviewRepo.findByProductId(productId);
        return productReviewConverter.toDtoList(reviews);
    }
}
