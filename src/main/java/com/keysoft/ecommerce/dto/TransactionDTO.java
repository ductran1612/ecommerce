package com.keysoft.ecommerce.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class TransactionDTO extends BaseDTO{
    private Long id;
    private String code;
    private CustomerDTO customer;
    private String name;
    private String address;
    private String email;
    private Integer status;
    private Boolean enable;
    private BigDecimal billInvoice;
    private String note;
    private List<TransactionDetailDTO> transactionDetails;

    private List<Long> deletedDetails;
}
