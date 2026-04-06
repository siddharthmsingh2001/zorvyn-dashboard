package com.zorvyn.financedashboard.repositories;

import com.zorvyn.financedashboard.entities.FinanceRecord;
import com.zorvyn.financedashboard.entities.RecordType;
import com.zorvyn.financedashboard.entities.User;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class FinanceRecordSpecification {

    // Only show records belonging to the logged-in user
    public static Specification<FinanceRecord> belongsToUser(User user) {
        return (root, query, cb) -> cb.equal(root.get("user"), user);
    }

    // Only show non-deleted records
    public static Specification<FinanceRecord> isNotDeleted() {
        return (root, query, cb) -> cb.equal(root.get("isDeleted"), false);
    }

    // Filter by Income or Expense
    public static Specification<FinanceRecord> hasType(RecordType type) {
        return (root, query, cb) -> type == null ? null : cb.equal(root.get("type"), type);
    }

    // Filter by Category ID
    public static Specification<FinanceRecord> hasCategory(Integer categoryId) {
        return (root, query, cb) -> categoryId == null ? null : cb.equal(root.get("category").get("id"), categoryId);
    }

    // Filter by Date Range (inclusive)
    public static Specification<FinanceRecord> betweenDates(LocalDate startDate, LocalDate endDate) {
        return (root, query, cb) -> {
            if (startDate == null && endDate == null) return null;
            if (startDate != null && endDate != null) {
                return cb.between(root.get("transactionDate"), startDate, endDate);
            }
            if (startDate != null) {
                return cb.greaterThanOrEqualTo(root.get("transactionDate"), startDate);
            }
            return cb.lessThanOrEqualTo(root.get("transactionDate"), endDate);
        };
    }

}
