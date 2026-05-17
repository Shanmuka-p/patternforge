package com.example.patternforge.patterns.bridge;

import com.example.patternforge.domain.DashboardComponent;

public abstract class BridgeWidget implements DashboardComponent {
    protected WidgetRenderer renderer;

    public void setRenderer(WidgetRenderer renderer) {
        this.renderer = renderer;
    }
}
