package com.example.patternforge.patterns.decorator;

import com.example.patternforge.domain.DashboardComponent;
import com.example.patternforge.domain.RenderResult;
import com.example.patternforge.tracing.PatternStackTracer;

/**
 * A concrete decorator that emits a custom trace event before delegating to
 * the parent decorator chain.
 *
 * <p>This class is intentionally thin — its only responsibility is to inject
 * an explicit, named trace entry ("Explicit Custom Log Entry") into the
 * WebSocket call-chain stream before {@code super.render()} propagates the
 * call downward through the decorator stack.</p>
 */
public class LoggingDecorator extends WidgetDecorator {

    public LoggingDecorator(DashboardComponent wrapped) {
        super(wrapped);
    }

    @Override
    public RenderResult render() {
        PatternStackTracer.trace("Decorator", "LoggingDecorator", "Explicit Custom Log Entry");
        return super.render();
    }
}
