package com.keysoft.ecommerce.constant;

public enum ProductStatusEnum {
    IN_STOCK(2),
    OUT_OF_STOCK(0),
    LOW_STOCK(1);

    public final Integer status;

    ProductStatusEnum(Integer status) {
        this.status = status;
    }
}