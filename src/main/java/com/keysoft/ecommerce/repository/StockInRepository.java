package com.keysoft.ecommerce.repository;

import com.keysoft.ecommerce.model.StockIn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface StockInRepository extends JpaRepository<StockIn, Long>, JpaSpecificationExecutor<StockIn> {
}
