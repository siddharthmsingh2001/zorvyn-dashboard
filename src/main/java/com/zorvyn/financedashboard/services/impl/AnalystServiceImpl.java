package com.zorvyn.financedashboard.services.impl;

import com.zorvyn.financedashboard.dtos.FinanceRecordResponse;
import com.zorvyn.financedashboard.entities.RecordType;
import com.zorvyn.financedashboard.mapper.FinanceRecordMapper;
import com.zorvyn.financedashboard.repositories.FinanceRecordRepository;
import com.zorvyn.financedashboard.services.AnalystService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AnalystServiceImpl implements AnalystService {

    private final FinanceRecordRepository recordRepository;
    private final FinanceRecordMapper recordMapper;

    public Map<String, BigDecimal> getGlobalOverview() {
        BigDecimal income = recordRepository.sumGlobalAmountByType(RecordType.INCOME);
        BigDecimal expense = recordRepository.sumGlobalAmountByType(RecordType.EXPENSE);

        return Map.of(
                "totalPlatformIncome", income != null ? income : BigDecimal.ZERO,
                "totalPlatformExpense", expense != null ? expense : BigDecimal.ZERO,
                "platformNetBalance", (income != null ? income : BigDecimal.ZERO).subtract(expense != null ? expense : BigDecimal.ZERO)
        );
    }

    public List<Map<String, Object>> getCategoryInsights() {
        return recordRepository.getGlobalCategorySpending().stream().map(obj -> Map.of(
                "categoryName", obj[0],
                "totalSpent", obj[1]
        )).toList();
    }

    public List<FinanceRecordResponse> getAllPlatformRecords() {
        return recordMapper.toResponseList(recordRepository.findAll());
    }

}
