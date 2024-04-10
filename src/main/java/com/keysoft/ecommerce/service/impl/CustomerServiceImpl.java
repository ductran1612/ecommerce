package com.keysoft.ecommerce.service.impl;

import com.keysoft.ecommerce.dto.CustomerDTO;
import com.keysoft.ecommerce.dto.UserDTO;
import com.keysoft.ecommerce.model.Customer;
import com.keysoft.ecommerce.model.Group;
import com.keysoft.ecommerce.model.User;
import com.keysoft.ecommerce.repository.CustomerRepository;
import com.keysoft.ecommerce.repository.GroupRepository;
import com.keysoft.ecommerce.repository.UserRepository;
import com.keysoft.ecommerce.service.CustomerService;
import com.keysoft.ecommerce.service.GroupService;
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
    private GroupRepository groupRepository;
    @Autowired
    private UserRepository userRepository;
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
        Customer customer = customerRepository.findById(Long.valueOf(id)).orElse(null);
        if (customer == null) {
            return false;
        }
        customer.setEnable(false);
        customerRepository.save(customer);
        return !customerRepository.findById(Long.valueOf(id)).orElse(null).getEnable();
    }

    @Override
    public CustomerDTO get(String id) {
        return modelMapper.map(customerRepository.findById(Long.valueOf(id)).orElse(null), CustomerDTO.class);
    }

    @Override
    public boolean checkExistUsername(CustomerDTO criteria) {
        Customer customer = customerRepository.findByUsername(criteria.getUsername()).orElse(null);

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
        Customer customer;
        User user;

        if(customerDTO.getId() == null) {
            if(checkExistUsername(customerDTO)) {
                return false;
            }
        }
        customer = modelMapper.map(customerDTO, Customer.class);
        user = modelMapper.map(customerDTO, User.class);
        customer.setEnable(true);
        user.setEnable(true);

        Group group = groupRepository.findByCode("khach-hang").orElse(null);
        user.setGroup(group);
        userRepository.save(user);
        return modelMapper.map(customerRepository.save(customer), UserDTO.class).getId() != null;
    }
}
