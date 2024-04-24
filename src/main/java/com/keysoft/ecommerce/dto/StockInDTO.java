package com.keysoft.ecommerce.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
public class StockInDTO extends BaseDTO{
    private Long id;
    private String code;
    private BigDecimal billInvoice;
    private Set<StockInDetailDTO> stockInDetails;
    private LocalDateTime createdDate;
    private Boolean enable;
}
