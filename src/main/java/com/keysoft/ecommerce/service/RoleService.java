package com.keysoft.ecommerce.service;

import com.keysoft.ecommerce.dto.CategoryDTO;
import com.keysoft.ecommerce.dto.RoleDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface RoleService {
    List<RoleDTO> getAllRoles();
    boolean save(RoleDTO roleDTO);

    RoleDTO get(Long id);

    boolean delete(Long id);
}
