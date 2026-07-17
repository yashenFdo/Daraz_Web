package com.daraz.web.repo;

import com.daraz.web.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepo extends JpaRepository<Product,String> {

    boolean existsBySku(String sku);

    boolean existsBySkuAndIdNot(String sku, String id);

    java.util.List<Product> findAllByIsDeletedFalse();

    @Query("SELECT p FROM Product p WHERE " +
            "(:searchText IS NULL OR LOWER(p.productName) LIKE LOWER(CONCAT('%', :searchText, '%'))) AND " +
            "(:minPrice IS NULL OR p.priceAfterDiscount >= :minPrice) AND " +
            "(:maxPrice IS NULL OR p.priceAfterDiscount <= :maxPrice) AND " +
            "(:stars IS NULL OR p.averageRating >= :stars) AND " +
            "(p.isActive = true AND p.isDeleted = false)") // Don't show disabled or deleted products
    Page<Product> findProductsByDynamicFilters(
            @Param("searchText") String searchText,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            @Param("stars") Integer stars,
            Pageable pageable
    );
}