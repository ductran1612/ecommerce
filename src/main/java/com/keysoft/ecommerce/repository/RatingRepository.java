package com.keysoft.ecommerce.repository;

import com.keysoft.ecommerce.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RatingRepository extends JpaRepository<Long, Rating>, JpaSpecificationExecutor<Rating> {
}
