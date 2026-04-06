package com.zorvyn.financedashboard.dtos;

import java.math.BigDecimal;

public record FinanceSummaryDto(
        BigDecimal totalIncome,
        BigDecimal totalExpense,
        BigDecimal netBalance
) {
}
