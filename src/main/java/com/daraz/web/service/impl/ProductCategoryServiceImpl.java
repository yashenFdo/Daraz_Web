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
        return productCategoryConverter.toDto(saved);
    }

    @Override
    public ProductCategoryResponseDTO modify(Long id, ProductCategoryRequestDTO dto) {
        return productCategoryRepo.findById(id)
                .map(category -> {
                    category.setName(dto.getName());
                    category.setCategoryImage(dto.getCategoryImage());
                    category.setParentCategory(productCategoryConverter.mapParentCategory(dto.getParentId()));
                    ProductCategory updated = productCategoryRepo.save(category);
                    return productCategoryConverter.toDto(updated);
                })
                .orElseThrow(() -> new EntryNotFoundException("NO Such Product Category. given id: " + id));
    }

    @Override
    public boolean remove(Long id) {
        if (!productCategoryRepo.existsById(id)) {
            throw new EntryNotFoundException("NO Such Product Category. given id: " + id);
        }
        productCategoryRepo.deleteById(id);
        return true;
    }

    @Override
    public ProductCategoryResponseDTO viewById(Long id) {
        return productCategoryRepo.findById(id)
                .map(productCategoryConverter::toDto)
                .orElseThrow(() -> new EntryNotFoundException("NO Such Product Category. given id: " + id));
    }

    @Override
    public List<ProductCategoryResponseDTO> viewAll() {
        List<ProductCategory> all = productCategoryRepo.findAll();
        return all.stream()
                .map(productCategoryConverter::toDto)
                .toList();
    }
}