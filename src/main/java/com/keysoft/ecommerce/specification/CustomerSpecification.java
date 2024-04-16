package com.keysoft.ecommerce.specification;

import com.keysoft.ecommerce.dto.CustomerDTO;
import com.keysoft.ecommerce.dto.ProductDTO;
import com.keysoft.ecommerce.model.Customer;
import com.keysoft.ecommerce.model.Product;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CustomerSpecification {
    public Specification<Customer> filterByName(final String keyword) {

        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("enable"), true));

            predicates.add(cb.like(cb.upper(root.get("fullName")), "%" + keyword.toUpperCase() + "%"));

            return cb.and(predicates.toArray(Predicate[]::new));
        };

    }

    public Specification<Customer> filter(CustomerDTO customerDTO) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("enable"), true));
            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }
}
