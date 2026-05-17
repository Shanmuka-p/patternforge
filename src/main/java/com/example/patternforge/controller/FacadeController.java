package com.example.patternforge.controller;

import com.example.patternforge.patterns.facade.DashboardManager;
import com.example.patternforge.patterns.facade.LayoutEngine;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/facade")
public class FacadeController {

    private final DashboardManager dashboardManager;
    private final LayoutEngine layoutEngine;

    public FacadeController(DashboardManager dashboardManager, LayoutEngine layoutEngine) {
        this.dashboardManager = dashboardManager;
        this.layoutEngine = layoutEngine;
    }

    @PostMapping("/create-dashboard")
    public List<String> createDashboard() {
        return dashboardManager.createDashboard();
    }

    @PostMapping("/subsystems/layout/calculate-grid")
    public String calculateGrid() {
        return layoutEngine.calculateGrid();
    }
}
