package com.zorvyn.financedashboard.repositories;

import com.zorvyn.financedashboard.entities.User;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.criteria.Predicate;

public class UserSpecification {

    public static Specification<User> filterUsers(String roleName, Boolean enabled, Boolean locked) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("isDeleted"), false));
            if (roleName != null && !roleName.isEmpty()) {
                predicates.add(cb.equal(root.get("role").get("name"), roleName));
            }
            if (enabled != null) {
                predicates.add(cb.equal(root.get("enabled"), enabled));
            }
            if (locked != null) {
                predicates.add(cb.equal(root.get("locked"), locked));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}