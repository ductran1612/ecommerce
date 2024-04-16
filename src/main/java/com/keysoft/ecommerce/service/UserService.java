package com.keysoft.ecommerce.service;

import com.keysoft.ecommerce.dto.UserDTO;
import org.springframework.data.domain.Page;

public interface UserService {
    Page<UserDTO> getAllUsers(UserDTO userDTO);
    boolean save(UserDTO userDTO);
    boolean delete(String id);
    UserDTO get(String id);
    boolean assignRole(UserDTO userDTO);
}
