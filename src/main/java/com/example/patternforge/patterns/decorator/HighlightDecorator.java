package com.example.patternforge.patterns.decorator;

import com.example.patternforge.domain.DashboardComponent;
import com.example.patternforge.domain.RenderResult;

public class HighlightDecorator extends WidgetDecorator {

    public HighlightDecorator(DashboardComponent wrapped) {
        super(wrapped);
    }

    @Override
    public RenderResult render() {
        RenderResult baseResult = super.render();
        String newHtml = "<div style='background-color: yellow;'>" + baseResult.html() + "</div>";
        return new RenderResult(newHtml, baseResult.cssStyles()).withCssStyle("highlighted");
    }
}
