package com.keysoft.ecommerce.specification;

import com.keysoft.ecommerce.dto.ProductDTO;
import com.keysoft.ecommerce.model.Product;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductSpecification {
    public Specification<Product> filterByName(final String keyword) {

        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("enable"), true));

            predicates.add(cb.like(cb.upper(root.get("name")), "%" + keyword.toUpperCase() + "%"));

            return cb.and(predicates.toArray(Predicate[]::new));
        };

    }

    public Specification<Product> filter() {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("enable"), true));
            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }
}
