package com.example.patternforge.patterns.decorator;

import com.example.patternforge.domain.DashboardComponent;
import com.example.patternforge.domain.RenderResult;
import com.example.patternforge.tracing.PatternStackTracer;

import java.util.List;

public abstract class WidgetDecorator implements DashboardComponent {
    protected final DashboardComponent wrapped;

    public WidgetDecorator(DashboardComponent wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public String getId() {
        return wrapped.getId();
    }

    @Override
    public RenderResult render() {
        PatternStackTracer.trace("Decorator", this.getClass().getSimpleName(), "render");
        return wrapped.render();
    }

    @Override
    public String getPatternInfo() {
        return wrapped.getPatternInfo();
    }

    @Override
    public void add(DashboardComponent child) {
        wrapped.add(child);
    }

    @Override
    public void remove(DashboardComponent child) {
        wrapped.remove(child);
    }

    @Override
    public List<DashboardComponent> getChildren() {
        return wrapped.getChildren();
    }
}
