package com.example.patternforge.patterns.bridge;

import com.example.patternforge.domain.RenderResult;

public interface WidgetRenderer {
    RenderResult renderData(String id, String type, String content);
}
