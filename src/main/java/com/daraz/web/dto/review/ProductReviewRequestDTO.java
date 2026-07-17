package com.daraz.web.dto.review;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductReviewRequestDTO {
    private int numOfStars;
    private String comment;
    private String customerId;
    private String productId;
}
