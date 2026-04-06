package com.zorvyn.financedashboard.dtos;

import com.zorvyn.financedashboard.entities.RecordType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record FinanceRecordResponse(
        UUID id,
        BigDecimal amount,
        RecordType type,
        CategoryDto category,
        String description,
        LocalDate transactionDate
) {
}
