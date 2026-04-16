package com.daraz.web.entity;

import com.daraz.web.enums.ProductCategory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : yashen
 * @created : 4/14/26
 * @project : web
 * @email : yashensavindu@gmail.com
 * @since : 0.1.0
 **/

@Entity
@Table(name = "product")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @Column(name = "product_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String productId;

    @Column(name = "sku", unique = true, nullable = false, length = 100)
    private String sku;

    @Column(name = "product_name", nullable = false, length = 255)
    private String productName;

    @Column(name = "description", columnDefinition = "TEXT")
    private String productDescription;

    private String brandName;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private ProductCategory category;

    private String seller; // I'll  change once create Seller Entity
    private String ShopName; // same as above one.

    @Column(name = "qty_on_hand", nullable = false)
    private int quantityOnHand;

    @Column(name = "unit_size")
    private String sizeOfOneUnit;

    @Column(precision = 12, scale = 2)
    private BigDecimal originalPrice;

    @Column(precision = 12, scale = 2)
    private BigDecimal priceAfterDiscount;

    private float discountPercentage;

    @Column(columnDefinition = "TEXT")
    private String productImageUrlMain;

    @Column(columnDefinition = "TEXT")
    private String productImageUrl1;

    @Column(columnDefinition = "TEXT")
    private String productImageUrl2;

    @Column(columnDefinition = "TEXT")
    private String productImageUrl3;

    @Column(name = "average_rating", columnDefinition = "DOUBLE DEFAULT 0.0")
    private double averageRating;

    @Column(name = "review_count", columnDefinition = "INT DEFAULT 0")
    private int reviewCount;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProductReview> reviews = new ArrayList<>();

    public void updateRating(int newStar) {
        if (this.reviewCount == 0) {
            this.averageRating = newStar;
            this.reviewCount = 1;
        } else {
            // running average
            double currentTotalStars = this.averageRating * this.reviewCount;
            this.reviewCount++;
            this.averageRating = (currentTotalStars + newStar) / this.reviewCount;
        }
    }
}