package com.daraz.web.controller;

import com.daraz.web.dto.cart.CartItemRequestDTO;
import com.daraz.web.entity.Customer;
import com.daraz.web.entity.Product;
import com.daraz.web.entity.ProductCategory;
import com.daraz.web.repo.CartRepo;
import com.daraz.web.repo.CustomerRepo;
import com.daraz.web.repo.ProductCategoryRepo;
import com.daraz.web.repo.ProductRepo;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@org.springframework.security.test.context.support.WithMockUser(username = "test-user@example.com", roles = {"USER"})
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CartRepo cartRepo;

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private ProductCategoryRepo productCategoryRepo;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JavaMailSender mailSender; // Mock to bypass mail sending in welcome email listener

    private Customer testCustomer;
    private Product testProduct;
    private ProductCategory testCategory;

    @BeforeEach
    void setUp() {
        testCategory = new ProductCategory();
        testCategory.setName("Cart Category " + UUID.randomUUID().toString().substring(0, 8));
        testCategory = productCategoryRepo.save(testCategory);

        testProduct = new Product();
        testProduct.setSku("sku-cart-" + UUID.randomUUID().toString().substring(0, 8));
        testProduct.setProductName("Cart Product");
        testProduct.setCategory(testCategory);
        testProduct.setOriginalPrice(BigDecimal.valueOf(100.00));
        testProduct.setDiscountPercentage(BigDecimal.valueOf(10.00));
        testProduct.setPriceAfterDiscount(BigDecimal.valueOf(90.00)); // calculated in lifecycles
        testProduct.setQuantityOnHand(5);
        testProduct.setWarranty("1 Year");
        testProduct = productRepo.save(testProduct);

        testCustomer = new Customer();
        testCustomer.setFirstName("Cart");
        testCustomer.setLastName("User");
        testCustomer.setEmail("cart-" + UUID.randomUUID().toString().substring(0, 8) + "@example.com");
        testCustomer.setMobileNumber("+9477" + (int)(Math.random() * 9000000 + 1000000));
        testCustomer.setNic("cart-" + UUID.randomUUID().toString().substring(0, 8));
        testCustomer = customerRepo.save(testCustomer);
    }

    @AfterEach
    void tearDown() {
        cartRepo.deleteAll();
        productRepo.deleteAll();
        customerRepo.deleteAll();
        productCategoryRepo.deleteAll();
    }

    @Test
    void testCartWorkflow() throws Exception {
        // 1. Fetch empty cart (should auto-create empty cart)
        mockMvc.perform(get("/api/v1/cart/" + testCustomer.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseCode").value(200))
                .andExpect(jsonPath("$.data.items", hasSize(0)))
                .andExpect(jsonPath("$.data.totalAmount").value(0.00));

        // 2. Add 2 items to cart (90.00 * 2 = 180.00 total)
        CartItemRequestDTO addDto = CartItemRequestDTO.builder()
                .productId(testProduct.getId())
                .quantity(2)
                .build();

        mockMvc.perform(post("/api/v1/cart/" + testCustomer.getId() + "/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseCode").value(200))
                .andExpect(jsonPath("$.data.items", hasSize(1)))
                .andExpect(jsonPath("$.data.items[0].quantity").value(2))
                .andExpect(jsonPath("$.data.items[0].itemTotal").value(180.00))
                .andExpect(jsonPath("$.data.totalAmount").value(180.00));

        // 3. Add 4 more items (total 6, which exceeds stock quantity of 5 -> should fail with 400 Bad Request)
        CartItemRequestDTO exceedDto = CartItemRequestDTO.builder()
                .productId(testProduct.getId())
                .quantity(4)
                .build();

        mockMvc.perform(post("/api/v1/cart/" + testCustomer.getId() + "/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(exceedDto)))
                .andExpect(status().isBadRequest()); // 400 due to IllegalArgumentException

        // 4. Update quantity to 3 (90.00 * 3 = 270.00 total)
        mockMvc.perform(put("/api/v1/cart/" + testCustomer.getId() + "/update/" + testProduct.getId())
                        .param("quantity", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.items[0].quantity").value(3))
                .andExpect(jsonPath("$.data.totalAmount").value(270.00));

        // 5. Remove item from cart
        mockMvc.perform(delete("/api/v1/cart/" + testCustomer.getId() + "/remove/" + testProduct.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.items", hasSize(0)))
                .andExpect(jsonPath("$.data.totalAmount").value(0.00));
    }
}
