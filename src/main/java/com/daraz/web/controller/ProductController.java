package com.daraz.web.controller;

import com.daraz.web.dto.product.PaginatedProductResponseDTO;
import com.daraz.web.dto.product.ProductFilterRequestDTO;
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

    @PatchMapping("/modify/{id}")
    public ResponseEntity<StandardResponse> modifyProduct(@PathVariable String id, @RequestBody ProductRequestDTO productRequestDTO){
        ProductResponseDTO modifiedProduct  = productService.modify(id, productRequestDTO);
        return new ResponseEntity<>(
                new StandardResponse(
                        200,
                        "Product Modified!",
                        modifiedProduct
                ),HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<StandardResponse> removeProduct(@PathVariable String id){
        boolean isRemove = productService.remove(id);
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(
                        200,
                        "Deleted!",
                        id
                ),HttpStatus.OK
        );
    }

    @GetMapping("/search")
    public ResponseEntity<StandardResponse> searchProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            ProductFilterRequestDTO filter
    ) {

        PaginatedProductResponseDTO filteredProducts = productService.viewFilteredProducts(page, size, filter);

        return new ResponseEntity<>(
                new StandardResponse(
                        200,
                        "Filtered",
                        filteredProducts

                ),HttpStatus.OK
        );
    }



}