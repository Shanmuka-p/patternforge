package com.example.patternforge.patterns.decorator;

import com.example.patternforge.domain.DashboardComponent;
import com.example.patternforge.domain.RenderResult;

public class BorderDecorator extends WidgetDecorator {

    public BorderDecorator(DashboardComponent wrapped) {
        super(wrapped);
    }

    @Override
    public RenderResult render() {
        RenderResult baseResult = super.render();
        String newHtml = "<div style='border: 2px solid green;'>" + baseResult.html() + "</div>";
        return new RenderResult(newHtml, baseResult.cssStyles()).withCssStyle("border-active");
    }
}
