package com.keysoft.ecommerce.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "stock_in")
public class StockIn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "bill_invoice")
    private BigDecimal billInvoice;

    @OneToMany(mappedBy = "stockIn", cascade = CascadeType.ALL)
    private Set<StockInDetail> stockInDetail;

    @Column(name = "created_date")
    private LocalDateTime createdDate;
}
