package com.daraz.web.controller;

import com.daraz.web.dto.review.ProductReviewRequestDTO;
import com.daraz.web.entity.Customer;
import com.daraz.web.entity.Product;
import com.daraz.web.entity.ProductCategory;
import com.daraz.web.repo.CustomerRepo;
import com.daraz.web.repo.ProductCategoryRepo;
import com.daraz.web.repo.ProductRepo;
import com.daraz.web.repo.ProductReviewRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@org.springframework.security.test.context.support.WithMockUser(username = "test-user@example.com", roles = {"USER"})
class ProductReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductReviewRepo productReviewRepo;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private ProductCategoryRepo productCategoryRepo;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JavaMailSender mailSender; // Mock to prevent actual mail sending on customer welcome hooks

    private ProductCategory testCategory;
    private Product testProduct;
    private Customer testCustomer;

    @BeforeEach
    void setUp() {
        testCategory = new ProductCategory();
        testCategory.setName("Electronics Reviews category " + UUID.randomUUID().toString().substring(0, 8));
        testCategory = productCategoryRepo.save(testCategory);

        testProduct = new Product();
        testProduct.setSku("sku-review-" + UUID.randomUUID().toString().substring(0, 8));
        testProduct.setProductName("Reviewable Smartphone");
        testProduct.setOriginalPrice(BigDecimal.valueOf(150.00));
        testProduct.setCategory(testCategory);
        testProduct.setQuantityOnHand(10);
        testProduct.setWarranty("1 Year");
        testProduct = productRepo.save(testProduct);

        testCustomer = new Customer();
        testCustomer.setFirstName("Reviewer");
        testCustomer.setLastName("User");
        testCustomer.setEmail("reviewer-" + UUID.randomUUID().toString().substring(0, 8) + "@example.com");
        testCustomer.setMobileNumber("+9477" + (int)(Math.random() * 9000000 + 1000000));
        testCustomer.setNic("nic-" + UUID.randomUUID().toString().substring(0, 8));
        testCustomer = customerRepo.save(testCustomer);
    }

    @AfterEach
    void tearDown() {
        productReviewRepo.deleteAll();
        productRepo.deleteAll();
        customerRepo.deleteAll();
        productCategoryRepo.deleteAll();
    }

    @Test
    void testCreateReviewAndRecalculateRating() throws Exception {
        // 1. Submit 1st review: 4 Stars
        ProductReviewRequestDTO review1 = ProductReviewRequestDTO.builder()
                .customerId(testCustomer.getId())
                .productId(testProduct.getId())
                .numOfStars(4)
                .comment("Really good product!")
                .build();

        mockMvc.perform(post("/api/v1/review/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(review1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.responseCode").value(201))
                .andExpect(jsonPath("$.data.numOfStars").value(4))
                .andExpect(jsonPath("$.data.customerName").value("Reviewer User"));

        // Verify product rating in DB is 4.0
        Product productAfter1 = productRepo.findById(testProduct.getId()).orElse(null);
        assertNotNull(productAfter1);
        assertEquals(4.0, productAfter1.getAverageRating(), 0.001);
        assertEquals(1, productAfter1.getReviewCount());

        // 2. Submit 2nd review: 5 Stars
        ProductReviewRequestDTO review2 = ProductReviewRequestDTO.builder()
                .customerId(testCustomer.getId())
                .productId(testProduct.getId())
                .numOfStars(5)
                .comment("Excellent quality!")
                .build();

        mockMvc.perform(post("/api/v1/review/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(review2)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.responseCode").value(201))
                .andExpect(jsonPath("$.data.numOfStars").value(5));

        // Verify running average rating recalculated: (4.0 + 5.0) / 2 = 4.5
        Product productAfter2 = productRepo.findById(testProduct.getId()).orElse(null);
        assertNotNull(productAfter2);
        assertEquals(4.5, productAfter2.getAverageRating(), 0.001);
        assertEquals(2, productAfter2.getReviewCount());

        // 3. Get reviews for the product
        mockMvc.perform(get("/api/v1/review/product/" + testProduct.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseCode").value(200))
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data[0].comment", anyOf(is("Really good product!"), is("Excellent quality!"))))
                .andExpect(jsonPath("$.data[1].comment", anyOf(is("Really good product!"), is("Excellent quality!"))));
    }
}
