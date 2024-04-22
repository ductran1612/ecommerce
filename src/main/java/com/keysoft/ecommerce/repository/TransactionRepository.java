package com.keysoft.ecommerce.repository;

import com.keysoft.ecommerce.model.Customer;
import com.keysoft.ecommerce.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long>, JpaSpecificationExecutor<Transaction> {
    List<Transaction> findAllByCustomer(Customer customer);
}
