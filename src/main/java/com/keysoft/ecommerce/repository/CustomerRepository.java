package com.keysoft.ecommerce.repository;

import com.keysoft.ecommerce.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByUsernameAndEnableTrue(String username);
    Optional<Customer> findByUsername(String username);
    Page<Customer> findAllByEnableTrue(Pageable pageable);
}
