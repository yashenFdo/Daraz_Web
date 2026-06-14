package com.daraz.web.dto.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author : yashen
 * @created : 5/10/26
 * @project : web
 * @email : yashensavindu@gmail.com
 * @since : 0.1.0
 **/

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaginatedProductResponseDTO {
    private List<ProductResponseDTO> productResponseDTOList;
    private int dataCount;
}