package com.keysoft.ecommerce.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class ProductDTO extends BaseDTO{
    private Long id;
    private String name;
    private String code;
    private BigDecimal price;
    private BigDecimal importPrice;
    private String description;
    private Integer quantity;
    private List<ProductImageDTO> images;
    private Set<CategoryDTO> categories;
    private List<Long> productCategories;
    private Boolean enable;
}
