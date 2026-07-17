package com.daraz.web.controller;

import com.daraz.web.dto.product.ProductRequestDTO;
import com.daraz.web.entity.Product;
import com.daraz.web.entity.ProductCategory;
import com.daraz.web.repo.ProductCategoryRepo;
import com.daraz.web.repo.ProductRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@org.springframework.security.test.context.support.WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private ProductCategoryRepo productCategoryRepo;

    @Autowired
    private ObjectMapper objectMapper;

    private ProductCategory testCategory;
    private Product testProduct;
    private String productSku;

    @BeforeEach
    void setUp() {
        testCategory = new ProductCategory();
        testCategory.setName("Test Electronics " + UUID.randomUUID().toString().substring(0, 8));
        testCategory = productCategoryRepo.save(testCategory);

        productSku = "sku-" + UUID.randomUUID().toString().substring(0, 8);
        testProduct = new Product();
        testProduct.setSku(productSku);
        testProduct.setProductName("Initial Product");
        testProduct.setCategory(testCategory);
        testProduct.setOriginalPrice(BigDecimal.valueOf(100.00));
        testProduct.setDiscountPercentage(BigDecimal.valueOf(10.00));
        testProduct.setPriceAfterDiscount(BigDecimal.valueOf(90.00));
        testProduct.setQuantityOnHand(20);
        testProduct.setWarranty("1 Year");
        testProduct = productRepo.save(testProduct);
    }

    @AfterEach
    void tearDown() {
        productRepo.deleteAll();
        productCategoryRepo.deleteAll();
    }

    @Test
    void testModifyProductSuccess() throws Exception {
        ProductRequestDTO requestDto = new ProductRequestDTO(
                productSku,
                "Updated Product Name",
                "New Description",
                "New Brand",
                "2 Years Warranty",
                testCategory,
                15,
                "One Unit",
                BigDecimal.valueOf(200.00),
                BigDecimal.valueOf(20.00), // 20% discount on 200 => 160
                "http://example.com/main.jpg",
                "http://example.com/1.jpg",
                "http://example.com/2.jpg",
                "http://example.com/3.jpg"
        );

        mockMvc.perform(patch("/api/v1/product/modify/" + testProduct.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseCode").value(200))
                .andExpect(jsonPath("$.message").value("Product Modified!"))
                .andExpect(jsonPath("$.data.productName").value("Updated Product Name"))
                .andExpect(jsonPath("$.data.originalPrice").value(200.00))
                .andExpect(jsonPath("$.data.priceAfterDiscount").value(160.00)) // calculated by @PreUpdate
                .andExpect(jsonPath("$.data.discountPercentage").value(20.00));
    }

    @Test
    void testModifyProductDuplicateSku() throws Exception {
        // Create another product
        Product otherProduct = new Product();
        String otherSku = "other-sku-" + UUID.randomUUID().toString().substring(0, 8);
        otherProduct.setSku(otherSku);
        otherProduct.setProductName("Other Product");
        otherProduct.setCategory(testCategory);
        otherProduct.setOriginalPrice(BigDecimal.valueOf(50.00));
        otherProduct.setQuantityOnHand(5);
        otherProduct.setWarranty("None");
        otherProduct = productRepo.save(otherProduct);

        // Try to update testProduct to use otherProduct's SKU
        ProductRequestDTO requestDto = new ProductRequestDTO(
                otherSku, // duplicate SKU
                "Updated Product Name",
                "Description",
                "Brand",
                "None",
                testCategory,
                15,
                "One Unit",
                BigDecimal.valueOf(100.00),
                BigDecimal.valueOf(0.00),
                null, null, null, null
        );

        mockMvc.perform(patch("/api/v1/product/modify/" + testProduct.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isConflict()) // Conflict (409) due to DuplicateEntryException
                .andExpect(jsonPath("$.responseCode").value(409))
                .andExpect(jsonPath("$.message").value("This SKU is already registered to another product!"));
    }

    @Test
    void testSoftDeleteProduct() throws Exception {
        // Soft delete the product
        mockMvc.perform(delete("/api/v1/product/" + testProduct.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseCode").value(200))
                .andExpect(jsonPath("$.message").value("Deleted!"))
                .andExpect(jsonPath("$.data").value(testProduct.getId()));

        // Assert isDeleted = true in database
        Product deletedProduct = productRepo.findById(testProduct.getId()).orElse(null);
        assertNotNull(deletedProduct);
        assertTrue(deletedProduct.isDeleted());

        // Get by ID endpoint should return 404 Not Found since we added a filter to viewById
        mockMvc.perform(get("/api/v1/product/" + testProduct.getId()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.responseCode").value(404));
    }
}
