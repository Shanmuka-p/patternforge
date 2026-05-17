package com.example.patternforge.patterns.proxy;

import com.example.patternforge.domain.RenderResult;
import com.example.patternforge.tracing.PatternStackTracer;

import java.util.List;

public class LazyWidgetProxy implements HeavyWidget {
    private final String id;
    private VideoWidget videoWidget;
    private String state = "UNLOADED";

    public LazyWidgetProxy(String id) {
        this.id = id;
    }

    @Override
    public void loadData() {
        if (videoWidget == null) {
            state = "LOADING";
            videoWidget = new VideoWidget(id);
            videoWidget.loadData();
            state = "LOADED";
        }
    }

    public String getState() {
        return state;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public RenderResult render() {
        PatternStackTracer.trace("Proxy", "LazyWidgetProxy", "render");
        if (videoWidget == null) {
            return new RenderResult("<div id=\"" + id + "\" class=\"skeleton\">Loading video...</div>", List.of());
        }
        return videoWidget.render();
    }

    @Override
    public String getPatternInfo() {
        return "Proxy Pattern: Virtual Proxy (LazyWidgetProxy)";
    }
}
