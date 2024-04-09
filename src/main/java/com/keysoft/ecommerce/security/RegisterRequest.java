package com.keysoft.ecommerce.security;

import com.keysoft.ecommerce.model.Role;
import lombok.Data;

@Data
public class RegisterRequest {
    private String fullName;
    private String username;
    private String password;
    private Role role;
}
