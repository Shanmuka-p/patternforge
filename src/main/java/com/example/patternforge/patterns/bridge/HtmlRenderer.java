package com.example.patternforge.patterns.bridge;

import com.example.patternforge.domain.RenderResult;

import java.util.List;

public class HtmlRenderer implements WidgetRenderer {
    @Override
    public RenderResult renderData(String id, String type, String content) {
        String html = String.format("<div id=\"%s\" class=\"%s\">%s</div>", id, type, content);
        return new RenderResult(html, List.of());
    }
}
