package com.zorvyn.financedashboard.controllers;

import com.zorvyn.financedashboard.dtos.APIResponse;
import com.zorvyn.financedashboard.dtos.FinanceRecordRequest;
import com.zorvyn.financedashboard.dtos.FinanceRecordResponse;
import com.zorvyn.financedashboard.dtos.FinanceSummaryDto;
import com.zorvyn.financedashboard.entities.RecordType;
import com.zorvyn.financedashboard.security.UserPrincipal;
import com.zorvyn.financedashboard.services.FinanceRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Finance Records", description = "Core financial management endpoints for logging transactions, viewing history, and calculating summaries.")
@SecurityRequirement(name = "cookieAuth")
public class FinanceRecordController {

    private final FinanceRecordService recordService;

    @Operation(summary = "Get recent activity", description = "Retrieves the 5 most recent transactions (Income or Expense) for the authenticated user.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Recent activity retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Authentication required")
    })
    @GetMapping("/recent")
    public ResponseEntity<APIResponse<List<FinanceRecordResponse>>> recentActivity(
            @AuthenticationPrincipal UserPrincipal principal
    ){
        var response = recordService.getRecentActivity(principal.getUser());
        return ResponseEntity.ok(APIResponse.created(response, "Recent activity fetched"));
    }

    @Operation(summary = "Log a new transaction", description = "Creates a new Income or Expense record. If categoryId is null, the record is automatically assigned to 'Miscellaneous'.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Record created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error in transaction data")
    })
    @PostMapping
    public ResponseEntity<APIResponse<FinanceRecordResponse>> create(
            @Valid @RequestBody FinanceRecordRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {
        var response = recordService.createRecord(request, principal.getUser());
        return ResponseEntity.ok(APIResponse.created(response, "Record added"));
    }

    @Operation(summary = "Search and filter records", description = "Retrieves a filtered list of records using optional criteria like type, category, or date range.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Filtered records retrieved")
    })
    @GetMapping
    public ResponseEntity<APIResponse<List<FinanceRecordResponse>>> list(
            @Parameter(description = "Filter by INCOME or EXPENSE") @RequestParam(required = false) RecordType type,
            @Parameter(description = "Filter by specific Category ID") @RequestParam(required = false) Integer categoryId,
            @Parameter(description = "Start date (YYYY-MM-DD)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "End date (YYYY-MM-DD)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @AuthenticationPrincipal UserPrincipal principal) {

        var list = recordService.getFilteredRecords(principal.getUser(), type, categoryId, startDate, endDate);
        return ResponseEntity.ok(APIResponse.ok(list, "Records retrieved"));
    }

    @Operation(summary = "Get financial summary", description = "Calculates total income, total expense, and net balance. Supports the same filters as the list endpoint for dynamic dashboard updates.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Summary calculated successfully")
    })
    @GetMapping("/summary")
    public ResponseEntity<APIResponse<FinanceSummaryDto>> summary(
            @Parameter(description = "Filter summary by Category ID") @RequestParam(required = false) Integer categoryId,
            @Parameter(description = "Start date for calculation (YYYY-MM-DD)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "End date for calculation (YYYY-MM-DD)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @AuthenticationPrincipal UserPrincipal principal) {

        var summary = recordService.getSummary(principal.getUser(), categoryId, startDate, endDate);
        return ResponseEntity.ok(APIResponse.ok(summary, "Financial summary calculated"));
    }

    @Operation(summary = "Delete a record", description = "Performs a soft-delete on a specific finance record. The record will no longer appear in history or summaries.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Record deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Record not found or access denied")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<Void>> delete(
            @Parameter(description = "UUID of the record to delete") @PathVariable UUID id,
            @AuthenticationPrincipal UserPrincipal principal) {
        recordService.deleteRecord(id, principal.getUser());
        return ResponseEntity.ok(APIResponse.ok(null, "Record deleted successfully"));
    }

}
