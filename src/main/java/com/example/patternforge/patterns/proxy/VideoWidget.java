package com.example.patternforge.patterns.proxy;

import com.example.patternforge.domain.RenderResult;

import java.util.List;

public class VideoWidget implements HeavyWidget {
    private final String id;
    private boolean isLoaded = false;

    public VideoWidget(String id) {
        this.id = id;
    }

    @Override
    public void loadData() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        isLoaded = true;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public RenderResult render() {
        if (isLoaded) {
            return new RenderResult("<video id=\"" + id + "\" autoplay></video>", List.of());
        } else {
            return new RenderResult("<div id=\"" + id + "\">Video broken (not loaded)</div>", List.of());
        }
    }

    @Override
    public String getPatternInfo() {
        return "Proxy Pattern: Real Subject (VideoWidget)";
    }
}
