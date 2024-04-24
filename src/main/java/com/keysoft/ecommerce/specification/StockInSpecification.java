package com.keysoft.ecommerce.specification;

import com.keysoft.ecommerce.model.StockIn;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class StockInSpecification {
    public Specification<StockIn> filter(String keyword) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("enable"), true));
            predicates.add(cb.like(cb.upper(root.get("code")), "%" + keyword.toUpperCase() + "%"));
            return cb.and(predicates.toArray(Predicate[]::new));
        };

    }
}
