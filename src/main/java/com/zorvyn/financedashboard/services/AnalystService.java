package com.zorvyn.financedashboard.services;

import com.zorvyn.financedashboard.dtos.FinanceRecordResponse;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface AnalystService {

    Map<String, BigDecimal> getGlobalOverview();

    List<Map<String, Object>> getCategoryInsights();

    List<FinanceRecordResponse> getAllPlatformRecords();

}
