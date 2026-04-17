package com.daraz.web.dto.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author : yashen
 * @created : 4/17/26
 * @project : web
 * @email : yashensavindu@gmail.com
 * @since : 0.1.0
 **/

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ProductCategoryResponseDTO {
    private Long id;
    private String name;
    private String categoryImage;
    private List<String> subCategoryNames;
}