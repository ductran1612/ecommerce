package com.keysoft.ecommerce.constant;

public enum TransactionStatusEnum {
    SUCCESS(3),
    CONFIRMED(2),
    CANCEL(0),
    PROGRESS(1);

    public final Integer status;

    TransactionStatusEnum(Integer status) {
        this.status = status;
    }
}