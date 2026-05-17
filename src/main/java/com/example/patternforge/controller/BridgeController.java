package com.example.patternforge.controller;

import com.example.patternforge.patterns.bridge.RendererService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class BridgeController {

    private final RendererService rendererService;

    public BridgeController(RendererService rendererService) {
        this.rendererService = rendererService;
    }

    public record RendererRequest(String renderer) {}

    @PutMapping({"/api/bridge/dashboard/renderer", "/api/dashboard/renderer"})
    public void updateRenderer(@RequestBody RendererRequest request) {
        rendererService.setActiveRenderer(request.renderer());
    }

    @GetMapping("/api/bridge/class-count")
    public Map<String, Integer> getClassCount() {
        return Map.of("withBridge", 6, "withoutBridge", 9);
    }
}
