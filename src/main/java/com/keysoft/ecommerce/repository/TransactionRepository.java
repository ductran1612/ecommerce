package com.keysoft.ecommerce.repository;

import com.keysoft.ecommerce.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
