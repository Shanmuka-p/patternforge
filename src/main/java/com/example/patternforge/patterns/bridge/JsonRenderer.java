package com.example.patternforge.patterns.bridge;

import com.example.patternforge.domain.RenderResult;

import java.util.List;

public class JsonRenderer implements WidgetRenderer {
    @Override
    public RenderResult renderData(String id, String type, String content) {
        String json = String.format("{ \"id\": \"%s\", \"type\": \"%s\", \"content\": \"%s\" }", id, type, content);
        return new RenderResult(json, List.of());
    }
}
