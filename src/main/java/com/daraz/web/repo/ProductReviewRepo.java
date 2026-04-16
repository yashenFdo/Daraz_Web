package com.daraz.web.repo;

import com.daraz.web.entity.ProductReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductReviewRepo extends JpaRepository<ProductReview,String> {

}