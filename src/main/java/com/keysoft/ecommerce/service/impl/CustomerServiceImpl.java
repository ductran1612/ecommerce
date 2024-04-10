package com.keysoft.ecommerce.service.impl;

import com.keysoft.ecommerce.dto.CustomerDTO;
import com.keysoft.ecommerce.model.Customer;
import com.keysoft.ecommerce.repository.CustomerRepository;
import com.keysoft.ecommerce.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public Page<CustomerDTO> getAllCustomers(CustomerDTO criteria) {
        log.info("service: get all customers");
        Page<Customer> page = customerRepository.findAll(PageRequest.of(criteria.getPage(), criteria.getSize()));
        List<CustomerDTO> result = new ArrayList<>();

        for(Customer item : page.getContent()) {
            result.add(modelMapper.map(item, CustomerDTO.class));
        }
        return new PageImpl<>(result, PageRequest.of(criteria.getPage(), criteria.getSize()), page.getTotalElements());
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, Throwable.class})
    public boolean delete(String id) {
        return false;
    }

    @Override
    public CustomerDTO get(String id) {
        return modelMapper.map(customerRepository.findById(Long.valueOf(id)).orElse(null), CustomerDTO.class);
    }

    @Override
    public boolean checkExistUsername(CustomerDTO criteria) {
        Customer customer = customerRepository.findByName(criteria.getUsername()).orElse(null);

        if (customer == null)
            return false;

        if (criteria.getId() == null) {
            return true;
        }

        return (!Objects.equals(customer.getId(), criteria.getId()));
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, Throwable.class})
    public boolean save(CustomerDTO customerDTO) {
        return false;
    }
}
