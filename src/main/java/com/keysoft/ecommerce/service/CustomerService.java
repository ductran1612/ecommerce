package com.keysoft.ecommerce.service;

import com.keysoft.ecommerce.dto.CustomerDTO;
import org.springframework.data.domain.Page;

public interface CustomerService {
    Page<CustomerDTO> getAllCustomers(CustomerDTO criteria);
    boolean delete(String id);
    CustomerDTO get(String id);
    boolean checkExistUsername(CustomerDTO criteria);
    boolean save(CustomerDTO customerDTO);
}
