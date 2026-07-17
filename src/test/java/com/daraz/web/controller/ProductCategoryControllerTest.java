package com.daraz.web.controller;

import com.daraz.web.dto.category.ProductCategoryRequestDTO;
import com.daraz.web.entity.ProductCategory;
import com.daraz.web.repo.ProductCategoryRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProductCategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductCategoryRepo productCategoryRepo;

    @Autowired
    private ObjectMapper objectMapper;

    private ProductCategory testCategory;
    private String uniqueName;

    @BeforeEach
    void setUp() {
        uniqueName = "Original Category " + UUID.randomUUID().toString().substring(0, 8);
        ProductCategory category = new ProductCategory();
        category.setName(uniqueName);
        category.setCategoryImage("http://example.com/original.jpg");
        testCategory = productCategoryRepo.save(category);
    }

    @AfterEach
    void tearDown() {
        if (testCategory != null && testCategory.getId() != null) {
            try {
                productCategoryRepo.deleteById(testCategory.getId());
            } catch (Exception e) {
                // Ignore if already deleted by a test
            }
        }
    }

    @Test
    void testCreateProductCategory() throws Exception {
        String newName = "New Category " + UUID.randomUUID().toString().substring(0, 8);
        ProductCategoryRequestDTO requestDto = ProductCategoryRequestDTO.builder()
                .name(newName)
                .categoryImage("http://example.com/new.jpg")
                .build();

        MvcResult result = mockMvc.perform(post("/api/v1/product_category/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.responseCode").value(201))
                .andExpect(jsonPath("$.message").value("New Product Category Added!"))
                .andExpect(jsonPath("$.data.name").value(newName))
                .andExpect(jsonPath("$.data.categoryImage").value("http://example.com/new.jpg"))
                .andReturn();

        // Cleanup the created category
        String content = result.getResponse().getContentAsString();
        com.jayway.jsonpath.ReadContext ctx = com.jayway.jsonpath.JsonPath.parse(content);
        Number id = ctx.read("$.data.id");
        if (id != null) {
            productCategoryRepo.deleteById(id.longValue());
        }
    }

    @Test
    void testGetProductCategoryById() throws Exception {
        mockMvc.perform(get("/api/v1/product_category/" + testCategory.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseCode").value(200))
                .andExpect(jsonPath("$.message").value("Product Category Found"))
                .andExpect(jsonPath("$.data.id").value(testCategory.getId()))
                .andExpect(jsonPath("$.data.name").value(uniqueName));
    }

    @Test
    void testUpdateProductCategory() throws Exception {
        String updatedName = "Updated Category " + UUID.randomUUID().toString().substring(0, 8);
        ProductCategoryRequestDTO updateRequest = ProductCategoryRequestDTO.builder()
                .name(updatedName)
                .categoryImage("http://example.com/updated.jpg")
                .build();

        mockMvc.perform(put("/api/v1/product_category/modify/" + testCategory.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseCode").value(200))
                .andExpect(jsonPath("$.message").value("Product Category Updated"))
                .andExpect(jsonPath("$.data.name").value(updatedName))
                .andExpect(jsonPath("$.data.categoryImage").value("http://example.com/updated.jpg"));
    }

    @Test
    void testGetAllProductCategories() throws Exception {
        mockMvc.perform(get("/api/v1/product_category"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseCode").value(200))
                .andExpect(jsonPath("$.data", hasSize(greaterThanOrEqualTo(1))));
    }

    @Test
    void testDeleteProductCategory() throws Exception {
        mockMvc.perform(delete("/api/v1/product_category/" + testCategory.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseCode").value(200))
                .andExpect(jsonPath("$.message").value("Product Category Deleted"))
                .andExpect(jsonPath("$.data").value(true));

        mockMvc.perform(get("/api/v1/product_category/" + testCategory.getId()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.responseCode").value(404));
    }
}
