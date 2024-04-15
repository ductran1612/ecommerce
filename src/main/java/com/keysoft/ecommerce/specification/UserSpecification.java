package com.keysoft.ecommerce.specification;

import com.keysoft.ecommerce.dto.UserDTO;
import com.keysoft.ecommerce.model.User;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserSpecification {
    public Specification<User> filterByName(final String keyword) {

        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("enable"), true));

            predicates.add(cb.like(cb.upper(root.get("name")), "%" + keyword.toUpperCase() + "%"));

            return cb.and(predicates.toArray(Predicate[]::new));
        };

    }

    public Specification<User> filter(UserDTO UserDTO) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("enable"), true));
            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }
}
