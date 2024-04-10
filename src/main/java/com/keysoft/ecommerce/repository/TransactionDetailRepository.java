package com.keysoft.ecommerce.repository;

import com.keysoft.ecommerce.model.TransactionDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionDetailRepository extends JpaRepository<TransactionDetail, Long> {
}
