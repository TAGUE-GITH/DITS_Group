package com.dits.dits_group.dashboard.controller;

import com.dits.dits_group.dashboard.dto.DashboardStatsResponse;
import com.dits.dits_group.dashboard.service.DashboardService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(
            DashboardService dashboardService
    ) {
        this.dashboardService =
                dashboardService;
    }

    // ==========================================
    // ADMIN : STATISTIQUES DU DASHBOARD
    // ==========================================

    @GetMapping("/stats")
    public ResponseEntity<DashboardStatsResponse> getStatistics() {

        DashboardStatsResponse statistics =
                dashboardService.getStatistics();

        return ResponseEntity.ok(
                statistics
        );
    }
}