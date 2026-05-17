package com.example.patternforge.domain;

import java.util.ArrayList;
import java.util.List;

public record RenderResult(String html, List<String> cssStyles) {

    public RenderResult {
        cssStyles = List.copyOf(cssStyles);
    }

    public RenderResult withCssStyle(String style) {
        List<String> newStyles = new ArrayList<>(this.cssStyles);
        newStyles.add(style);
        return new RenderResult(this.html, newStyles);
    }
}
