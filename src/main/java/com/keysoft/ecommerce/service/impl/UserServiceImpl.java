package com.keysoft.ecommerce.service.impl;

import com.keysoft.ecommerce.dto.UserDTO;
import com.keysoft.ecommerce.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Override
    public UserDTO findByUsername(String username) {
        return null;
    }
}
