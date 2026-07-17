package com.daraz.web.dto.review;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductReviewResponseDTO {
    private String reviewId;
    private int numOfStars;
    private String comment;
    private LocalDateTime date;
    private String customerName;
    private String productName;
}
