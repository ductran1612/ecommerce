package com.keysoft.ecommerce.dto;

import java.util.Set;

public class UserDTO {
    private Long id;
    private String username;
    private String password;
    private String fullName;
    private String address;
    private String phone;
    private String email;
    private GroupDTO groupDTO;
    private Set<RoleDTO> roles;
}
