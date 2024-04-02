package com.keysoft.ecommerce.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CategoryDTO {
    private Long id;
    private String name;
    private String code;
    private Long parentsId;
    private CategoryDTO parentsCategory;
    private List<CategoryDTO> subCategories;
    private Boolean enable;
}
