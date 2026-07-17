package com.daraz.web.controller;

import com.daraz.web.dto.category.ProductCategoryRequestDTO;
import com.daraz.web.dto.category.ProductCategoryResponseDTO;
import com.daraz.web.service.ProductCategoryService;
import com.daraz.web.util.StandardResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author : yashen
 * @created : 4/22/26
 * @project : web
 * @email : yashensavindu@gmail.com
 * @since : 0.1.0
 **/
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/product_category")
public class ProductCategoryController {
    private final ProductCategoryService productCategoryService;

    @PostMapping("/create")
    public ResponseEntity<StandardResponse> creatProductCategory(@RequestBody ProductCategoryRequestDTO productCategoryRequestDTO){
        ProductCategoryResponseDTO savedProductCategory = productCategoryService.save(productCategoryRequestDTO);
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(
                        201,
                        "New Product Category Added!",
                        savedProductCategory
                ), HttpStatus.CREATED
        );
    }

    @PutMapping("/modify/{id}")
    public ResponseEntity<StandardResponse> updateProductCategory(@PathVariable Long id, @RequestBody ProductCategoryRequestDTO productCategoryRequestDTO){
        ProductCategoryResponseDTO updatedCategory = productCategoryService.modify(id, productCategoryRequestDTO);
        return new ResponseEntity<>(
                new StandardResponse(
                        200,
                        "Product Category Updated",
                        updatedCategory
                ), HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<StandardResponse> getProductCategory(@PathVariable Long id){
        ProductCategoryResponseDTO category = productCategoryService.viewById(id);
        return new ResponseEntity<>(
                new StandardResponse(
                        200,
                        "Product Category Found",
                        category
                ), HttpStatus.OK
        );
    }

    @GetMapping("")
    public ResponseEntity<StandardResponse> getAllProductCategories(){
        List<ProductCategoryResponseDTO> categories = productCategoryService.viewAll();
        return new ResponseEntity<>(
                new StandardResponse(
                        200,
                        "Fetched " + categories.size() + " categories",
                        categories
                ), HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<StandardResponse> deleteProductCategory(@PathVariable Long id){
        boolean isDeleted = productCategoryService.remove(id);
        return new ResponseEntity<>(
                new StandardResponse(
                        200,
                        "Product Category Deleted",
                        isDeleted
                ), HttpStatus.OK
        );
    }
}