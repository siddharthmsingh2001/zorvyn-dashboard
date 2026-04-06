package com.zorvyn.financedashboard.dtos;

import com.zorvyn.financedashboard.entities.RecordType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record FinanceRecordRequest(
        @NotNull(message = "Amount is required")
        @Positive(message = "Amount must be positive")
        BigDecimal amount,

        @NotNull(message = "Record type (INCOME/EXPENSE) is required")
        RecordType type,

        Integer categoryId,

        String description,

        @NotNull(message = "Date is required")
        LocalDate transactionDate
) {
}
