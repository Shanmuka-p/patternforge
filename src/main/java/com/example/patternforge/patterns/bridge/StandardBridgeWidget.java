package com.example.patternforge.patterns.bridge;

import com.example.patternforge.domain.RenderResult;
import com.example.patternforge.tracing.PatternStackTracer;

public class StandardBridgeWidget extends BridgeWidget {
    private final String id;
    private final String type;
    private final String content;

    public StandardBridgeWidget(String id, String type, String content) {
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
        PatternStackTracer.trace("Bridge", "StandardBridgeWidget", "render");
        if (renderer == null) {
            throw new IllegalStateException("Renderer not set");
        }
        return renderer.renderData(id, type, content);
    }

    @Override
    public String getPatternInfo() {
        return "Bridge Pattern: Decoupled abstraction from implementation";
    }
}
