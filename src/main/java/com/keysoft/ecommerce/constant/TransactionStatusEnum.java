package com.keysoft.ecommerce.constant;

public enum TransactionStatusEnum {
    CONFIRMED(2),
    CANCEL(0),
    PROGRESS(1);

    public final Integer status;

    TransactionStatusEnum(Integer status) {
        this.status = status;
    }
}