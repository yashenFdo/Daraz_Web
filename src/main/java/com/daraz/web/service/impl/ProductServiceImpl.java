package com.daraz.web.service.impl;


import com.daraz.web.converter.ProductConverter;
import com.daraz.web.dto.product.*;
import com.daraz.web.entity.Product;
import com.daraz.web.enums.ProductSortOption;
import com.daraz.web.exception.custom.DuplicateEntryException;
import com.daraz.web.exception.custom.EntryNotFoundException;
import com.daraz.web.repo.ProductRepo;
import com.daraz.web.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @Override
    public PaginatedProductResponseDTO viewFilteredProducts(int page, int size, ProductFilterRequestDTO filter) {

        // 1. Resolve to default option (NEWEST) if sorting is not specified
        ProductSortOption sortOption = (filter.getSortBy() != null) ? filter.getSortBy() : ProductSortOption.NEWEST;
        String sortColumn = sortOption.getFieldName();

        // 2. Map the Enum to its corresponding Sort Strategy natively
        Sort sort = switch (sortOption) {
            case PRICE_DESC -> Sort.by(sortColumn).descending();
            case RATING     -> Sort.by(sortColumn).descending(); // Top rated first
            case NEWEST     -> Sort.by(sortColumn).descending(); // Newest arrivals first
            case PRICE_ASC  -> Sort.by(sortColumn).ascending();
            case NAME       -> Sort.by(sortColumn).ascending();  // Alphabetical A-Z
        };

        // 3. Package into Pageable for Server-Side Pagination
        Pageable pageable = PageRequest.of(page, size, sort);

        // 4. Hit the database with filters
        Page<Product> productPage = productRepo.findProductsByDynamicFilters(
                filter.getSearchText(),
                filter.getMinPrice(),
                filter.getMaxPrice(),
                filter.getRatingStars(),
                pageable
        );

        // 5. Convert entities to DTOs and return with metadata
        List<ProductResponseDTO> content = productConverter.toResponseDTOList(productPage.getContent());
        return new PaginatedProductResponseDTO(content, Math.toIntExact(productPage.getTotalElements()));
    }


}