package com.daraz.web.converter;

import com.daraz.web.dto.category.ProductCategoryRequestDTO;
import com.daraz.web.dto.category.ProductCategoryResponseDTO;
import com.daraz.web.dto.product.ProductRequestDTO;
import com.daraz.web.dto.product.ProductResponseDTO;
import com.daraz.web.entity.Product;
import com.daraz.web.entity.ProductCategory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductConverterTest {

    @Autowired
    private ProductConverter productConverter;

    @Autowired
    private ProductCategoryConverter productCategoryConverter;

    @Autowired
    private com.daraz.web.repo.ProductRepo productRepo;

    @Autowired
    private com.daraz.web.repo.ProductCategoryRepo productCategoryRepo;

    @Test
    void testProductPersistencePriceCalculation() {
        // 1. Create and save a ProductCategory
        ProductCategory category = new ProductCategory();
        category.setName("Electronics Test Category");
        category = productCategoryRepo.save(category);

        // 2. Create a Product
        Product product = new Product();
        product.setSku("test-sku-999");
        product.setProductName("Test Persisted Product");
        product.setOriginalPrice(BigDecimal.valueOf(200.00));
        product.setDiscountPercentage(BigDecimal.valueOf(10.00)); // 10% discount
        product.setCategory(category);
        product.setQuantityOnHand(5);
        product.setWarranty("No Warranty");

        // 3. Save to database (should trigger @PrePersist)
        Product savedProduct = productRepo.save(product);

        try {
            // Verify price after discount calculated automatically
            assertNotNull(savedProduct.getPriceAfterDiscount());
            // 200 - (10% of 200) = 180
            // Compare as double/float values to handle bigdecimal scale differences
            assertEquals(180.0, savedProduct.getPriceAfterDiscount().doubleValue(), 0.001);

            // 4. Update the discount to 15% and save (should trigger @PreUpdate)
            savedProduct.setDiscountPercentage(BigDecimal.valueOf(15.00));
            Product updatedProduct = productRepo.save(savedProduct);

            // 200 - (15% of 200) = 170
            assertEquals(170.0, updatedProduct.getPriceAfterDiscount().doubleValue(), 0.001);
        } finally {
            // Cleanup
            productRepo.delete(savedProduct);
            productCategoryRepo.delete(category);
        }
    }

    @Test
    void testProductMapping() {
        Product product = new Product();
        product.setId("prod-123");
        product.setProductName("Test Product");
        product.setOriginalPrice(BigDecimal.valueOf(100.00));
        product.setPriceAfterDiscount(BigDecimal.valueOf(80.00));
        product.setDiscountPercentage(BigDecimal.valueOf(20.00));
        product.setProductImageUrlMain("http://example.com/image.jpg");
        product.setQuantityOnHand(10);
        product.setActive(true);
        product.setDeleted(false);

        ProductResponseDTO dto = productConverter.toDto(product);

        assertNotNull(dto);
        assertEquals("prod-123", dto.getProductId());
        assertEquals("Test Product", dto.getProductName());
        assertEquals(BigDecimal.valueOf(100.00), dto.getOriginalPrice());
        assertEquals(BigDecimal.valueOf(80.00), dto.getPriceAfterDiscount());
        assertEquals("http://example.com/image.jpg", dto.getImageUrlMain());
        assertTrue(dto.isAvailable());
        assertEquals("In Stock", dto.getStockStatus());
    }

    @Test
    void testProductCategoryMapping() {
        ProductCategory parent = new ProductCategory();
        parent.setId(1L);
        parent.setName("Parent Category");

        ProductCategory subCategory1 = new ProductCategory();
        subCategory1.setId(2L);
        subCategory1.setName("Sub 1");
        subCategory1.setParentCategory(parent);

        ProductCategory subCategory2 = new ProductCategory();
        subCategory2.setId(3L);
        subCategory2.setName("Sub 2");
        subCategory2.setParentCategory(parent);

        parent.setSubCategories(List.of(subCategory1, subCategory2));

        ProductCategoryResponseDTO responseDto = productCategoryConverter.toDto(parent);

        assertNotNull(responseDto);
        assertEquals("Parent Category", responseDto.getName());
        assertNotNull(responseDto.getSubCategoryNames());
        assertEquals(2, responseDto.getSubCategoryNames().size());
        assertTrue(responseDto.getSubCategoryNames().contains("Sub 1"));
        assertTrue(responseDto.getSubCategoryNames().contains("Sub 2"));
    }
}
