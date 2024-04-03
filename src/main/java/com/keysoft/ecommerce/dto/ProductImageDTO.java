package com.keysoft.ecommerce.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductImageDTO {
    private Long id;
    private String name;
    private ProductDTO productDTO;
}
