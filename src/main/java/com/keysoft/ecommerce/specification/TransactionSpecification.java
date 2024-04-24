package com.keysoft.ecommerce.specification;

import com.keysoft.ecommerce.model.Transaction;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;

@Component
public class TransactionSpecification {
    public Specification<Transaction> filter(String keyword) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("enable"), true));
            predicates.add(cb.like(cb.upper(root.get("code")), "%" + keyword.toUpperCase() + "%"));
            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }
}
