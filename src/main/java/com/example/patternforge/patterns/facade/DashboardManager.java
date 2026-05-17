package com.example.patternforge.patterns.facade;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashboardManager {

    private final LayoutEngine layoutEngine;
    private final ThemeEngine themeEngine;
    private final WidgetRegistry widgetRegistry;
    private final RenderEngine renderEngine;

    public DashboardManager(LayoutEngine layoutEngine, ThemeEngine themeEngine, WidgetRegistry widgetRegistry, RenderEngine renderEngine) {
        this.layoutEngine = layoutEngine;
        this.themeEngine = themeEngine;
        this.widgetRegistry = widgetRegistry;
        this.renderEngine = renderEngine;
    }

    public List<String> createDashboard() {
        return List.of(
                widgetRegistry.registerDefaults(),
                layoutEngine.calculateGrid(),
                themeEngine.applyColors(),
                renderEngine.flushBuffer()
        );
    }

    public List<String> applyTheme() {
        return List.of(
                themeEngine.applyColors(),
                renderEngine.flushBuffer()
        );
    }
}
