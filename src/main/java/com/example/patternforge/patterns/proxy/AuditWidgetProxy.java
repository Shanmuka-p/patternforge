package com.example.patternforge.patterns.proxy;

import com.example.patternforge.domain.DashboardComponent;
import com.example.patternforge.domain.RenderResult;
import com.example.patternforge.patterns.decorator.WidgetDecorator;

import java.time.Instant;

public class AuditWidgetProxy extends WidgetDecorator {

    public AuditWidgetProxy(DashboardComponent wrapped) {
        super(wrapped);
    }

    @Override
    public RenderResult render() {
        AuditService.log("Widget rendered at " + Instant.now().toString());
        return super.render();
    }

    @Override
    public String getPatternInfo() {
        return "Proxy Pattern: Audit Proxy";
    }
}
