package com.keysoft.ecommerce.specification;

import com.keysoft.ecommerce.dto.CategoryDTO;
import com.keysoft.ecommerce.model.Category;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Component
public class CategorySpecification {
    public Specification<Category> filter() {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.equal(root.get("enable"), true));

            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }

    public Specification<Category> search(final Boolean isMain) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.equal(root.get("enable"), true));

            if (isMain != null) {
                Predicate mainCategoriesCheck = isMain ? cb.isNull(root.get("parentsId")) : cb.isNotNull(root.get("parentsId"));
                predicates.add(mainCategoriesCheck);
            }

            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }
}
