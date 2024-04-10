package com.keysoft.ecommerce.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TransactionDetailDTO extends BaseDTO{
    private Long id;
    private BigDecimal sellPrice;
    private BigDecimal total;
    private Integer quantity;
    private ProductDTO product;
}
