package com.daraz.web.controller;

import com.daraz.web.dto.review.ProductReviewRequestDTO;
import com.daraz.web.dto.review.ProductReviewResponseDTO;
import com.daraz.web.service.ProductReviewService;
import com.daraz.web.util.StandardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/review")
@RequiredArgsConstructor
public class ProductReviewController {

    private final ProductReviewService productReviewService;

    @PostMapping("/create")
    public ResponseEntity<StandardResponse> createReview(@RequestBody ProductReviewRequestDTO dto) {
        ProductReviewResponseDTO responseDTO = productReviewService.save(dto);
        return new ResponseEntity<>(
                new StandardResponse(
                        201,
                        "Product review added successfully!",
                        responseDTO
                ), HttpStatus.CREATED
        );
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<StandardResponse> getReviewsByProduct(@PathVariable String productId) {
        List<ProductReviewResponseDTO> reviews = productReviewService.getReviewsByProductId(productId);
        return new ResponseEntity<>(
                new StandardResponse(
                        200,
                        "Fetched " + reviews.size() + " product reviews",
                        reviews
                ), HttpStatus.OK
        );
    }
}
