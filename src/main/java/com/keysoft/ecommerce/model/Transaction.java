package com.keysoft.ecommerce.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "transaction")
@Getter
@Setter
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "code")
    private String code;

    @Column(name = "address")
    private String address;

    @Column(name = "note")
    private String note;

    @Column(name = "bill_invoice")
    private BigDecimal billInvoice;

    @Column(name = "create_date")
    private LocalDateTime createdDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(mappedBy = "transaction", cascade = {CascadeType.ALL})
    private Set<TransactionDetail> transactionDetails;

    @Column(name = "status")
    private Integer status = 1;

    @Column(name = "enable")
    private Boolean enable;
}
