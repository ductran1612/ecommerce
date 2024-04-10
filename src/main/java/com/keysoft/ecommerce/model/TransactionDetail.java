package com.keysoft.ecommerce.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "transaction_detail")
public class TransactionDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction", nullable = false)
    private Transaction transaction;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "sell_price", nullable = false, columnDefinition="decimal(13)")
    private BigDecimal sellPrice;

    @Column(name = "total_price", nullable = false, columnDefinition="decimal(13)")
    private BigDecimal total;
}