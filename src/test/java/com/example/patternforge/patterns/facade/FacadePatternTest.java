package com.example.patternforge.patterns.facade;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FacadePatternTest {

    @Test
    void testCreateDashboard() {
        DashboardManager facade = new DashboardManager(
                new LayoutEngine(),
                new ThemeEngine(),
                new WidgetRegistry(),
                new RenderEngine()
        );

        List<String> result = facade.createDashboard();
        assertEquals(4, result.size());
        assertEquals("Defaults registered", result.get(0));
        assertEquals("Grid calculated", result.get(1));
        assertEquals("Colors applied", result.get(2));
        assertEquals("Buffer flushed", result.get(3));
    }

    @Test
    void testApplyTheme() {
        DashboardManager facade = new DashboardManager(
                new LayoutEngine(),
                new ThemeEngine(),
                new WidgetRegistry(),
                new RenderEngine()
        );

        List<String> result = facade.applyTheme();
        assertEquals(2, result.size());
        assertEquals("Colors applied", result.get(0));
        assertEquals("Buffer flushed", result.get(1));
    }
}
