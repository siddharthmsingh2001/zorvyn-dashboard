package com.zorvyn.financedashboard.controllers;

import com.zorvyn.financedashboard.dtos.APIResponse;
import com.zorvyn.financedashboard.dtos.FinanceRecordResponse;
import com.zorvyn.financedashboard.services.AnalystService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Analyst Insights", description = "Platform-wide financial analytics and administrative record access. Restricted to Analyst and Admin roles.")
@SecurityRequirement(name = "cookieAuth")
public class AnalystController {

    private final AnalystService analystService;

    @Operation(
            summary = "Get global platform overview",
            description = "Calculates total income, expenses, and net flow across all users on the platform. Useful for monitoring system-wide financial volume."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Global overview successfully calculated"),
            @ApiResponse(responseCode = "403", description = "Access denied: Requires ANALYST or ADMIN role")
    })
    @GetMapping("/overview")
    public ResponseEntity<APIResponse<Map<String, BigDecimal>>> getOverview() {
        return ResponseEntity.ok(APIResponse.ok(analystService.getGlobalOverview(), "Global overview fetched"));
    }

    @Operation(
            summary = "Get global category spending trends",
            description = "Aggregates spending data by category across all users. Identifies which categories are most popular platform-wide."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Category insights successfully fetched"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping("/categories")
    public ResponseEntity<APIResponse<List<Map<String, Object>>>> getCategoryStats() {
        return ResponseEntity.ok(APIResponse.ok(analystService.getCategoryInsights(), "Category insights fetched"));
    }

    @Operation(
            summary = "View all platform records",
            description = "Retrieves every financial record in the system across all users. This is a high-privilege audit endpoint."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Full platform history retrieved"),
            @ApiResponse(responseCode = "403", description = "Access denied: Insufficient privileges")
    })
    @GetMapping("/all-records")
    public ResponseEntity<APIResponse<List<FinanceRecordResponse>>> getAllRecords() {
        return ResponseEntity.ok(APIResponse.ok(analystService.getAllPlatformRecords(), "Full platform history retrieved"));
    }

}
