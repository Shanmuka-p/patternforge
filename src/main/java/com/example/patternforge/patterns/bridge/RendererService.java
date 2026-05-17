package com.example.patternforge.patterns.bridge;

import org.springframework.stereotype.Service;

@Service
public class RendererService {
    private WidgetRenderer activeRenderer = new HtmlRenderer();

    public WidgetRenderer getActiveRenderer() {
        return activeRenderer;
    }

    public void setActiveRenderer(String type) {
        if ("json".equalsIgnoreCase(type)) {
            this.activeRenderer = new JsonRenderer();
        } else if ("html".equalsIgnoreCase(type)) {
            this.activeRenderer = new HtmlRenderer();
        } else if ("svg".equalsIgnoreCase(type)) {
            this.activeRenderer = new SvgRenderer();
        } else {
            throw new IllegalArgumentException("Unknown renderer: " + type);
        }
    }
}
