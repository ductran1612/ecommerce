package com.keysoft.ecommerce.constant;

public enum DefaultPageEnum {
    PAGE(0),  SIZE(10);

    private final int value;
    private DefaultPageEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
