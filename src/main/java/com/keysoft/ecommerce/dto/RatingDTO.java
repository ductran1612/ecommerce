package com.keysoft.ecommerce.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RatingDTO extends BaseDTO{
    private Long id;
    private Integer score;
    private String comment;
    private ProductDTO product;
    private CustomerDTO customer;
    private LocalDateTime createdDate;
}
