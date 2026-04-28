package com.daraz.web.controller;

import com.daraz.web.dto.product.ProductRequestDTO;
import com.daraz.web.dto.product.ProductResponseDTO;
import com.daraz.web.service.ProductService;
import com.daraz.web.util.StandardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author : yashen
 * @created : 4/10/26
 * @project : web
 * @email : yashensavindu@gmail.com
 * @since : 0.1.0
 **/


@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/{id}")
    public ResponseEntity<StandardResponse> viewProduct(@PathVariable String id){
        ProductResponseDTO productResponseDTO = productService.viewById(id);
        return new ResponseEntity<>(
                new StandardResponse(
                        200,
                        "Product Found!",
                        productResponseDTO
                ), HttpStatus.OK
        );
    }

    @PostMapping("/create")
    public ResponseEntity<StandardResponse> createProduct(@RequestBody ProductRequestDTO productRequestDTO){
        ProductResponseDTO savedProduct = productService.save(productRequestDTO);
        return new ResponseEntity<>(
                new StandardResponse(
                        201,
                        "New Product Added!",
                        savedProduct
                ),HttpStatus.CREATED
        );
    }
}