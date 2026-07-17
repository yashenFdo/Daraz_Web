package com.daraz.web.controller;

import com.daraz.web.dto.auth.AuthRequestDTO;
import com.daraz.web.dto.customer.CustomerDTO;
import com.daraz.web.repo.CartRepo;
import com.daraz.web.repo.CustomerRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private CartRepo cartRepo;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JavaMailSender mailSender; // Mock welcome emails

    @AfterEach
    void tearDown() {
        cartRepo.deleteAll();
        customerRepo.deleteAll();
    }

    @Test
    void testRegistrationLoginAndSecureEndpoint() throws Exception {
        String email = "secure-" + UUID.randomUUID().toString().substring(0, 8) + "@example.com";
        String nic = "nic-" + UUID.randomUUID().toString().substring(0, 8);
        String mobile = "+9477" + (int)(Math.random() * 9000000 + 1000000);

        CustomerDTO registerDto = new CustomerDTO(
                null,
                "Secure",
                "User",
                email,
                mobile,
                nic,
                "password123" // pass raw password for hashing
        );

        // 1. Register customer
        MvcResult regResult = mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.responseCode").value(201))
                .andExpect(jsonPath("$.data.email").value(email))
                .andReturn();

        String responseBody = regResult.getResponse().getContentAsString();
        String customerId = objectMapper.readTree(responseBody).path("data").path("id").asText();
        assertNotNull(customerId);

        // 2. Login customer -> obtain token
        AuthRequestDTO loginDto = new AuthRequestDTO(email, "password123");
        MvcResult loginResult = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseCode").value(200))
                .andExpect(jsonPath("$.data.token").exists())
                .andExpect(jsonPath("$.data.customerId").value(customerId))
                .andReturn();

        String loginResponseBody = loginResult.getResponse().getContentAsString();
        String token = objectMapper.readTree(loginResponseBody).path("data").path("token").asText();
        assertNotNull(token);

        // 3. Try to access cart without token -> expect 403 Forbidden (since authentication is required)
        mockMvc.perform(get("/api/v1/cart/" + customerId))
                .andExpect(status().isForbidden());

        // 4. Try to access cart with Bearer token -> expect 200 OK
        mockMvc.perform(get("/api/v1/cart/" + customerId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseCode").value(200))
                .andExpect(jsonPath("$.data.customerId").value(customerId));
    }
}
