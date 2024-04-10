package com.keysoft.ecommerce.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerDTO extends BaseDTO{
    private Long id;
    private String username;
    private String fullName;
    private String gender;
    private String address;
    private String phone;
    private String email;
    private Boolean enable;
}
