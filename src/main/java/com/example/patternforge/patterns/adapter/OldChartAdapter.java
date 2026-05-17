package com.example.patternforge.patterns.adapter;

import com.example.patternforge.domain.RenderResult;

import java.util.List;

public class OldChartAdapter implements ChartWidget {
    private final String id;
    private final List<Double> data;
    private final OldChartLib oldChartLib;

    public OldChartAdapter(String id, List<Double> data) {
        this.id = id;
        this.data = data;
        this.oldChartLib = new OldChartLib();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public RenderResult render() {
        double[] arrayData = data.stream().mapToDouble(Double::doubleValue).toArray();
        String output = oldChartLib.drawChart(arrayData);
        return new RenderResult("<div id=\"" + id + "\">" + output + "</div>", List.of());
    }

    @Override
    public String getPatternInfo() {
        return "Adapter Pattern: Adapting OldChartLib";
    }

    @Override
    public String getAdapterTrace() {
        return "Converted List<Double> to double[] before calling OldChartLib.drawChart()";
    }
}
