package com.zorvyn.financedashboard.controllers;

import com.zorvyn.financedashboard.dtos.APIResponse;
import com.zorvyn.financedashboard.dtos.FinanceRecordRequest;
import com.zorvyn.financedashboard.dtos.FinanceRecordResponse;
import com.zorvyn.financedashboard.dtos.FinanceSummaryDto;
import com.zorvyn.financedashboard.entities.RecordType;
import com.zorvyn.financedashboard.security.UserPrincipal;
import com.zorvyn.financedashboard.services.FinanceRecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/records")
@RequiredArgsConstructor
public class FinanceRecordController {

    private final FinanceRecordService recordService;

    @PostMapping
    public ResponseEntity<APIResponse<FinanceRecordResponse>> create(
            @Valid @RequestBody FinanceRecordRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {
        var response = recordService.createRecord(request, principal.getUser());
        return ResponseEntity.ok(APIResponse.created(response, "Record added"));
    }

    @GetMapping
    public ResponseEntity<APIResponse<List<FinanceRecordResponse>>> list(
            @RequestParam(required = false) RecordType type,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @AuthenticationPrincipal UserPrincipal principal) {

        var list = recordService.getFilteredRecords(principal.getUser(), type, categoryId, startDate, endDate);
        return ResponseEntity.ok(APIResponse.ok(list, "Records retrieved"));
    }

    @GetMapping("/summary")
    public ResponseEntity<APIResponse<FinanceSummaryDto>> summary(
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @AuthenticationPrincipal UserPrincipal principal) {

        var summary = recordService.getSummary(principal.getUser(), categoryId, startDate, endDate);
        return ResponseEntity.ok(APIResponse.ok(summary, "Financial summary calculated"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<Void>> delete(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserPrincipal principal) {
        recordService.deleteRecord(id, principal.getUser());
        return ResponseEntity.ok(APIResponse.ok(null, "Record deleted successfully"));
    }

}
