package com.example.patternforge.patterns.adapter;

import com.example.patternforge.domain.RenderResult;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AdapterPatternTest {

    @Test
    void testLegacyGraphAdapter() {
        List<Double> data = List.of(1.0, 2.0, 3.0);
        ChartWidget adapter = new LegacyGraphAdapter("chart1", data);

        RenderResult result = adapter.render();
        assertTrue(result.html().contains("LegacyGraphLib plotting 3 points"));
        assertEquals("chart1", adapter.getId());
        assertEquals("Passed List<Double> directly to LegacyGraphLib.plot()", adapter.getAdapterTrace());
    }

    @Test
    void testOldChartAdapter() {
        List<Double> data = List.of(1.0, 2.0, 3.0);
        ChartWidget adapter = new OldChartAdapter("chart2", data);

        RenderResult result = adapter.render();
        assertTrue(result.html().contains("OldChartLib drawing chart with 3 points"));
        assertEquals("chart2", adapter.getId());
        assertEquals("Converted List<Double> to double[] before calling OldChartLib.drawChart()", adapter.getAdapterTrace());
    }
}
