package com.example.patternforge.controller;

import com.example.patternforge.domain.RenderResult;
import com.example.patternforge.patterns.adapter.ChartWidget;
import com.example.patternforge.patterns.adapter.LegacyGraphAdapter;
import com.example.patternforge.patterns.adapter.OldChartAdapter;
import com.example.patternforge.patterns.composite.DashboardTreeService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/widget")
public class AdapterController {

    private final DashboardTreeService dashboardTreeService;

    public AdapterController(DashboardTreeService dashboardTreeService) {
        this.dashboardTreeService = dashboardTreeService;
    }

    public static class ChartRequest {
        private String id;
        private List<Double> data;
        private String source;

        public ChartRequest() {}

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public List<Double> getData() { return data; }
        public void setData(List<Double> data) { this.data = data; }
        public String getSource() { return source; }
        public void setSource(String source) { this.source = source; }
    }

    @PostMapping("/chart")
    public ChartWidget createChart(@RequestBody ChartRequest request) {
        if (request.getSource() == null) {
            throw new IllegalArgumentException("Source cannot be null");
        }

        ChartWidget widget;
        switch (request.getSource().toLowerCase()) {
            case "legacy":
                widget = new LegacyGraphAdapter(request.getId(), request.getData());
                break;
            case "old":
                widget = new OldChartAdapter(request.getId(), request.getData());
                break;
            case "new":
                widget = new ChartWidget() {
                    @Override public String getAdapterTrace() { return "No adapter needed for new chart format"; }
                    @Override public String getId() { return request.getId(); }
                    @Override public RenderResult render() { return new RenderResult("<div id=\"" + request.getId() + "\">New Chart</div>", new ArrayList<>()); }
                    @Override public String getPatternInfo() { return "Standard Implementation"; }
                };
                break;
            default:
                throw new IllegalArgumentException("Unknown source: " + request.getSource());
        }
        
        dashboardTreeService.registerNode(widget);
        return widget;
    }

    @GetMapping("/{id}/adapter-trace")
    public String getAdapterTrace(@PathVariable String id) {
        ChartWidget widget = (ChartWidget) dashboardTreeService.getNode(id);
        if (widget == null) {
            throw new IllegalArgumentException("Widget not found");
        }
        return widget.getAdapterTrace();
    }
}
