package com.keysoft.ecommerce.service;

import com.keysoft.ecommerce.dto.UserDTO;

public interface UserService {
    UserDTO findByUsername(String username);
    boolean save(UserDTO userDTO);
    boolean delete(String id);
}
