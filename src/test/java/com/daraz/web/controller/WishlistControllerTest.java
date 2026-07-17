package com.daraz.web.controller;

import com.daraz.web.entity.Customer;
import com.daraz.web.entity.Product;
import com.daraz.web.entity.ProductCategory;
import com.daraz.web.repo.CustomerRepo;
import com.daraz.web.repo.ProductCategoryRepo;
import com.daraz.web.repo.ProductRepo;
import com.daraz.web.repo.WishlistRepo;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class WishlistControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WishlistRepo wishlistRepo;

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private ProductCategoryRepo productCategoryRepo;

    @MockBean
    private JavaMailSender mailSender; // Mock mail sender to bypass SMTP connections

    private Customer testCustomer;
    private Product testProduct;
    private ProductCategory testCategory;

    @BeforeEach
    void setUp() {
        testCategory = new ProductCategory();
        testCategory.setName("Wishlist Category " + UUID.randomUUID().toString().substring(0, 8));
        testCategory = productCategoryRepo.save(testCategory);

        testProduct = new Product();
        testProduct.setSku("sku-wishlist-" + UUID.randomUUID().toString().substring(0, 8));
        testProduct.setProductName("Wishlist Product");
        testProduct.setCategory(testCategory);
        testProduct.setOriginalPrice(BigDecimal.valueOf(120.00));
        testProduct.setQuantityOnHand(5);
        testProduct.setWarranty("None");
        testProduct = productRepo.save(testProduct);

        testCustomer = new Customer();
        testCustomer.setFirstName("Wishlist");
        testCustomer.setLastName("User");
        testCustomer.setEmail("wishlist-" + UUID.randomUUID().toString().substring(0, 8) + "@example.com");
        testCustomer.setMobileNumber("+9477" + (int)(Math.random() * 9000000 + 1000000));
        testCustomer.setNic("wlist-" + UUID.randomUUID().toString().substring(0, 8));
        testCustomer = customerRepo.save(testCustomer);
    }

    @AfterEach
    void tearDown() {
        wishlistRepo.deleteAll();
        productRepo.deleteAll();
        customerRepo.deleteAll();
        productCategoryRepo.deleteAll();
    }

    @Test
    void testWishlistWorkflow() throws Exception {
        // 1. Fetch wishlist (should auto-create empty wishlist)
        mockMvc.perform(get("/api/v1/wishlist/" + testCustomer.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseCode").value(200))
                .andExpect(jsonPath("$.data.customerId").value(testCustomer.getId()))
                .andExpect(jsonPath("$.data.products", hasSize(0)));

        // 2. Add product to wishlist
        mockMvc.perform(post("/api/v1/wishlist/" + testCustomer.getId() + "/add/" + testProduct.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseCode").value(200))
                .andExpect(jsonPath("$.message").value("Product added to wishlist successfully"))
                .andExpect(jsonPath("$.data.products", hasSize(1)))
                .andExpect(jsonPath("$.data.products[0].productId").value(testProduct.getId()))
                .andExpect(jsonPath("$.data.products[0].productName").value("Wishlist Product"));

        // 3. Add duplicate product (should not create duplicates)
        mockMvc.perform(post("/api/v1/wishlist/" + testCustomer.getId() + "/add/" + testProduct.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.products", hasSize(1)));

        // 4. Remove product from wishlist
        mockMvc.perform(delete("/api/v1/wishlist/" + testCustomer.getId() + "/remove/" + testProduct.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Product removed from wishlist successfully"))
                .andExpect(jsonPath("$.data.products", hasSize(0)));
    }
}
