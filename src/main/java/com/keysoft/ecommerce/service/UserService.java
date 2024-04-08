package com.keysoft.ecommerce.service;

import com.keysoft.ecommerce.dto.UserDTO;

public interface UserService {
    UserDTO findByUsername(String username);
}
