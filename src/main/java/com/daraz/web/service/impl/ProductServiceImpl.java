package com.daraz.web.service.impl;

import com.daraz.web.converter.CustomerConverter;
import com.daraz.web.converter.ProductConverter;
import com.daraz.web.dto.product.ProductRequestDTO;
import com.daraz.web.dto.product.ProductResponseDTO;
import com.daraz.web.entity.Product;
import com.daraz.web.exception.custom.DuplicateEntryException;
import com.daraz.web.exception.custom.EntryNotFoundException;
import com.daraz.web.repo.ProductRepo;
import com.daraz.web.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


/**
 * @author : yashen
 * @created : 4/17/26
 * @project : web
 * @email : yashensavindu@gmail.com
 * @since : 0.1.0
 **/

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepo productRepo;
    private final ProductConverter productConverter;
    private final CustomerConverter customerConverter;

    @Override
    public ProductResponseDTO save(ProductRequestDTO dto) {
        // check SKU already exists or not
        // if not, then move forwad
        if(productRepo.existsBySku(dto.getSku())){
            throw new DuplicateEntryException("This SKU is already registered!");
        }

        Product saved = productRepo.save(productConverter.toEntity(dto));
        return productConverter.toDto(saved);
    }

    @Override
    public ProductResponseDTO modify(String s, ProductRequestDTO dto) {
        return null;
    }

    @Override
    public boolean remove(String s) {
        return false;
    }

    @Override
    public ProductResponseDTO viewById(String id) {
         return productRepo.findById(id)
                 .map(product -> productConverter.toDto(product))
                 .orElseThrow(()-> new EntryNotFoundException("Product Not Found!"));
    }

    @Override
    public List<ProductResponseDTO> viewAll() {
        return null;
    }
}