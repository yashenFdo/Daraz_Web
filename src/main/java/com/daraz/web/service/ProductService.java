package com.daraz.web.service;

import com.daraz.web.dto.product.*;

/**
 * @author : yashen
 * @created : 4/17/26
 * @project : web
 * @email : yashensavindu@gmail.com
 * @since : 0.1.0
 **/
public interface ProductService extends SuperService<ProductRequestDTO, ProductResponseDTO,String> {
    public PaginatedProductResponseDTO viewFilteredProducts(int page, int size, ProductFilterRequestDTO filter);
}