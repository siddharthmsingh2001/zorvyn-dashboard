package com.zorvyn.financedashboard.repositories;

import com.zorvyn.financedashboard.entities.Category;
import com.zorvyn.financedashboard.entities.FinanceRecord;
import com.zorvyn.financedashboard.entities.RecordType;
import com.zorvyn.financedashboard.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface FinanceRecordRepository extends JpaRepository<FinanceRecord, UUID> , JpaSpecificationExecutor<FinanceRecord> {

    @Modifying
    @Query("UPDATE FinanceRecord f SET f.category = :newCat " +
            "WHERE f.category = :oldCat AND f.user = :user AND f.isDeleted = false")
    void updateCategoryForUser(Category oldCat, Category newCat, User user);

    @Query("SELECT SUM(f.amount) FROM FinanceRecord f WHERE f.type = :type AND f.isDeleted = false")
    BigDecimal sumGlobalAmountByType(RecordType type);

    @Query("SELECT f.category.name, SUM(f.amount) FROM FinanceRecord f " +
            "WHERE f.type = 'EXPENSE' AND f.isDeleted = false " +
            "GROUP BY f.category.name ORDER BY SUM(f.amount) DESC")
    List<Object[]> getGlobalCategorySpending();

    @Query("SELECT MONTH(f.transactionDate), YEAR(f.transactionDate), SUM(f.amount) " +
            "FROM FinanceRecord f WHERE f.type = 'EXPENSE' AND f.isDeleted = false " +
            "GROUP BY MONTH(f.transactionDate), YEAR(f.transactionDate) " +
            "ORDER BY YEAR(f.transactionDate) DESC, MONTH(f.transactionDate) DESC")
    List<Object[]> getGlobalMonthlyTrends();
    
}
