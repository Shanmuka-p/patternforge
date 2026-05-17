package com.example.patternforge.patterns.bridge;

import com.example.patternforge.domain.RenderResult;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BridgePatternTest {

    @Test
    void testStandardBridgeWidgetWithVariousRenderers() {
        StandardBridgeWidget widget = new StandardBridgeWidget("w1", "text-widget", "Hello World");

        // Test with HTML Renderer
        widget.setRenderer(new HtmlRenderer());
        RenderResult htmlResult = widget.render();
        assertEquals("<div id=\"w1\" class=\"text-widget\">Hello World</div>", htmlResult.html());

        // Test with JSON Renderer
        widget.setRenderer(new JsonRenderer());
        RenderResult jsonResult = widget.render();
        assertEquals("{ \"id\": \"w1\", \"type\": \"text-widget\", \"content\": \"Hello World\" }", jsonResult.html());

        // Test with SVG Renderer
        widget.setRenderer(new SvgRenderer());
        RenderResult svgResult = widget.render();
        assertEquals("<svg id=\"w1\" data-type=\"text-widget\"><text>Hello World</text></svg>", svgResult.html());
        
        assertEquals("w1", widget.getId());
    }
}
