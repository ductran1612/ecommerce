package com.keysoft.ecommerce.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
public class GroupDTO {
    private Long id;
    private String code;
    private String name;
    private Set<RoleDTO> roles;
    private List<Long> listId;
}
