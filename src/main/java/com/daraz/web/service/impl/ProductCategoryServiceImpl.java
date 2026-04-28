package com.daraz.web.service.impl;

import com.daraz.web.converter.ProductCategoryConverter;
import com.daraz.web.dto.category.ProductCategoryRequestDTO;
import com.daraz.web.dto.category.ProductCategoryResponseDTO;
import com.daraz.web.entity.ProductCategory;
import com.daraz.web.exception.custom.EntryNotFoundException;
import com.daraz.web.repo.ProductCategoryRepo;
import com.daraz.web.service.ProductCategoryService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : yashen
 * @created : 4/22/26
 * @project : web
 * @email : yashensavindu@gmail.com
 * @since : 0.1.0
 **/

@Service
@RequiredArgsConstructor

public class ProductCategoryServiceImpl implements ProductCategoryService {
    private final ProductCategoryRepo productCategoryRepo;
    private final ProductCategoryConverter productCategoryConverter;


    @Override
    public ProductCategoryResponseDTO save(ProductCategoryRequestDTO dto) {
        ProductCategory entity = productCategoryConverter.toEntity(dto);
        ProductCategory saved = productCategoryRepo.save(entity);
        return productCategoryConverter.toDto(entity);
    }

    @Override
    public ProductCategoryResponseDTO modify(Long id, ProductCategoryRequestDTO dto) {
        if(!productCategoryRepo.existsById(id)){
            throw new EntryNotFoundException("NO Such Product Category.");
        }
//        return productCategoryRepo.findById(id)
//                .map(productCategory -> productCategoryConverter.toEntity(dto));
        return null;
    }

    @Override
    public boolean remove(Long aLong) {
        return false;
    }

    @Override
    public ProductCategoryResponseDTO viewById(Long aLong) {
        return null;
    }

    @Override
    public List<ProductCategoryResponseDTO> viewAll() {
        return null;
    }
}