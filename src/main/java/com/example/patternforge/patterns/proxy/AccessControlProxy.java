package com.example.patternforge.patterns.proxy;

import com.example.patternforge.domain.DashboardComponent;
import com.example.patternforge.domain.RenderResult;
import com.example.patternforge.patterns.decorator.WidgetDecorator;

import java.util.List;

public class AccessControlProxy extends WidgetDecorator {
    private final String requiredRole;

    public AccessControlProxy(DashboardComponent wrapped, String requiredRole) {
        super(wrapped);
        this.requiredRole = requiredRole;
    }

    @Override
    public RenderResult render() {
        if (requiredRole.equals(SessionState.getCurrentRole())) {
            return super.render();
        } else {
            return new RenderResult("<div class=\"error\">Access Denied</div>", List.of());
        }
    }

    @Override
    public String getPatternInfo() {
        return "Proxy Pattern: Access Control Proxy";
    }
}
