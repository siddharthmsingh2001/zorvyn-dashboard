package com.zorvyn.financedashboard.controllers;

import com.zorvyn.financedashboard.dtos.APIResponse;
import com.zorvyn.financedashboard.dtos.FinanceRecordResponse;
import com.zorvyn.financedashboard.services.AnalystService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/analyst")
@RequiredArgsConstructor
public class AnalystController {

    private final AnalystService analystService;

    @GetMapping("/overview")
    public ResponseEntity<APIResponse<Map<String, BigDecimal>>> getOverview() {
        return ResponseEntity.ok(APIResponse.ok(analystService.getGlobalOverview(), "Global overview fetched"));
    }

    @GetMapping("/categories")
    public ResponseEntity<APIResponse<List<Map<String, Object>>>> getCategoryStats() {
        return ResponseEntity.ok(APIResponse.ok(analystService.getCategoryInsights(), "Category insights fetched"));
    }

    @GetMapping("/all-records")
    public ResponseEntity<APIResponse<List<FinanceRecordResponse>>> getAllRecords() {
        return ResponseEntity.ok(APIResponse.ok(analystService.getAllPlatformRecords(), "Full platform history retrieved"));
    }

}
