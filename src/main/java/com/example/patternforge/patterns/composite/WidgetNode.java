package com.example.patternforge.patterns.composite;

import com.example.patternforge.domain.DashboardComponent;
import com.example.patternforge.domain.RenderResult;
import com.example.patternforge.tracing.PatternStackTracer;

import java.util.List;

public class WidgetNode implements DashboardComponent {
    private final String id;
    private final String type;
    private final String content;

    public WidgetNode(String id, String type, String content) {
        this.id = id;
        this.type = type;
        this.content = content;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public RenderResult render() {
        PatternStackTracer.trace("Composite", "WidgetNode", "render");
        return new RenderResult("<div>" + content + "</div>", List.of());
    }

    @Override
    public String getPatternInfo() {
        return "Composite Pattern: Leaf node (" + type + ")";
    }
}
