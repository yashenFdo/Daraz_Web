package com.daraz.web.dto.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class ProductCategoryRequestDTO {
    private String name;
    private String categoryImage;
    private Long parentId; // Just the ID of the parent
}