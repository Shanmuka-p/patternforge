package com.example.patternforge.patterns.bridge;

import com.example.patternforge.domain.RenderResult;

import java.util.List;

public class SvgRenderer implements WidgetRenderer {
    @Override
    public RenderResult renderData(String id, String type, String content) {
        String svg = String.format("<svg id=\"%s\" data-type=\"%s\"><text>%s</text></svg>", id, type, content);
        return new RenderResult(svg, List.of());
    }
}
