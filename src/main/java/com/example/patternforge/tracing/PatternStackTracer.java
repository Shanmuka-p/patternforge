package com.example.patternforge.tracing;

import jakarta.annotation.PostConstruct;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Singleton Spring service that broadcasts {@link CallChainEvent}s to all
 * WebSocket subscribers on {@code /topic/call-chain}.
 *
 * <p>The static {@link #trace(String, String, String)} method is designed for
 * <em>explicit manual instrumentation</em>: any class in the application can
 * call it directly without requiring Spring injection, making it a perfect fit
 * for decorating design-pattern implementations by hand rather than via AOP.</p>
 *
 * <p>The instance reference is populated by {@link #init()} once Spring has
 * fully constructed and wired this bean.</p>
 */
@Service
public class PatternStackTracer {

    /** Destination topic consumed by front-end WebSocket clients. */
    private static final String TOPIC = "/topic/call-chain";

    /** Lazily populated with the Spring-managed instance after construction. */
    private static PatternStackTracer instance;

    private final SimpMessagingTemplate messagingTemplate;

    public PatternStackTracer(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * Stores the fully-initialized bean so that the static
     * {@link #trace(String, String, String)} method can forward events even
     * when called from non-Spring-managed code.
     */
    @PostConstruct
    private void init() {
        instance = this;
    }

    /**
     * Records a single traced invocation and pushes it over WebSocket.
     *
     * <p>Safe to call before the application context is fully started; if
     * {@code instance} is {@code null} the call is silently ignored.</p>
     *
     * @param patternType The design-pattern category (e.g. {@code "Facade"}).
     * @param className   The class whose method was traced.
     * @param methodName  The method that was traced.
     */
    public static void trace(String patternType, String className, String methodName) {
        if (instance == null) {
            return;
        }

        CallChainEvent event = new CallChainEvent(
                UUID.randomUUID().toString(),
                patternType,
                className,
                methodName,
                System.currentTimeMillis()
        );

        instance.messagingTemplate.convertAndSend(TOPIC, event);
    }
}
