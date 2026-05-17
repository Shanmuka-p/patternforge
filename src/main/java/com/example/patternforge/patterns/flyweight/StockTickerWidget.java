package com.example.patternforge.patterns.flyweight;

import com.example.patternforge.domain.DashboardComponent;
import com.example.patternforge.domain.RenderResult;
import com.example.patternforge.tracing.PatternStackTracer;

import java.util.List;

/**
 * Flyweight widget that displays a live stock ticker.
 *
 * <p><strong>Intrinsic state</strong> (shared, stored inside the flyweight):
 * {@code tickerSymbol} and {@code color} — these are set once at construction
 * and never change.</p>
 *
 * <p><strong>Extrinsic state</strong> (context-specific, supplied by the
 * client before each render): {@code price} and {@code change} — the caller
 * must set these via {@link #setExtrinsicState(double, double)} before calling
 * {@link #render()}.</p>
 */
public class StockTickerWidget implements DashboardComponent {

    // --- Intrinsic (shared) state ---
    private final String id;
    private final String tickerSymbol;
    private final String color;

    // --- Extrinsic (context) state ---
    private double price;
    private double change;

    public StockTickerWidget(String id, String tickerSymbol, String color) {
        this.id = id;
        this.tickerSymbol = tickerSymbol;
        this.color = color;
    }

    /**
     * Supplies the per-render extrinsic state.
     *
     * @param price  Current market price.
     * @param change Price change since previous close (positive = up, negative = down).
     */
    public void setExtrinsicState(double price, double change) {
        this.price = price;
        this.change = change;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public RenderResult render() {
        PatternStackTracer.trace("Flyweight", "StockTickerWidget", "render");
        String arrow = change >= 0 ? "▲" : "▼";
        String sign  = change >= 0 ? "+" : "";
        String html  = String.format(
                "<div id=\"%s\" class=\"stock-ticker\" style=\"color:%s\">" +
                "<span class=\"symbol\">%s</span> " +
                "<span class=\"price\">$%.2f</span> " +
                "<span class=\"change\">%s%s%.2f</span>" +
                "</div>",
                id, color, tickerSymbol, price, arrow, sign, change
        );
        return new RenderResult(html, List.of("stock-ticker"));
    }

    @Override
    public String getPatternInfo() {
        return "Flyweight Pattern: shared intrinsic state for ticker=" + tickerSymbol;
    }
}
