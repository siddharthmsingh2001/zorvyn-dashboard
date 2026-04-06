package com.zorvyn.financedashboard.services;

import com.zorvyn.financedashboard.dtos.FinanceRecordRequest;
import com.zorvyn.financedashboard.dtos.FinanceRecordResponse;
import com.zorvyn.financedashboard.dtos.FinanceSummaryDto;
import com.zorvyn.financedashboard.entities.Category;
import com.zorvyn.financedashboard.entities.RecordType;
import com.zorvyn.financedashboard.entities.User;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface FinanceRecordService {

    FinanceRecordResponse createRecord(FinanceRecordRequest request, User user);

    List<FinanceRecordResponse> getFilteredRecords(User user, RecordType type, Integer categoryId, LocalDate start, LocalDate end);

    FinanceSummaryDto getSummary(User user, Integer categoryId, LocalDate start, LocalDate end);

    void deleteRecord(UUID id, User user);

}
