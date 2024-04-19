package com.keysoft.ecommerce.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RatingDTO extends BaseDTO{
    private Long id;
    private String comment;
    private Integer rating;
    private ProductDTO product;
    private CustomerDTO customer;
    private LocalDateTime createdDate;
}
