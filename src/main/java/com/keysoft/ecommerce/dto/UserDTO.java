package com.keysoft.ecommerce.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UserDTO {
    private Long id;
    private String username;
    private String password;
    private String fullName;
    private String address;
    private String email;
    private GroupDTO groupDTO;
    private Set<RoleDTO> roles;
}
