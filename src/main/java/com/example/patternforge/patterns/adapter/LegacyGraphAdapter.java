package com.example.patternforge.patterns.adapter;

import com.example.patternforge.domain.RenderResult;

import java.util.List;

public class LegacyGraphAdapter implements ChartWidget {
    private final String id;
    private final List<Double> data;
    private final LegacyGraphLib legacyGraphLib;

    public LegacyGraphAdapter(String id, List<Double> data) {
        this.id = id;
        this.data = data;
        this.legacyGraphLib = new LegacyGraphLib();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public RenderResult render() {
        String output = legacyGraphLib.plot(data);
        return new RenderResult("<div id=\"" + id + "\">" + output + "</div>", List.of());
    }

    @Override
    public String getPatternInfo() {
        return "Adapter Pattern: Adapting LegacyGraphLib";
    }

    @Override
    public String getAdapterTrace() {
        return "Passed List<Double> directly to LegacyGraphLib.plot()";
    }
}
