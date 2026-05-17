package com.example.patternforge.tracing;

/**
 * Immutable event snapshot representing one traced method invocation in a
 * design-pattern call chain.
 *
 * @param id          Unique identifier for this event (random UUID).
 * @param patternType The design-pattern category (e.g. "Facade", "Singleton").
 * @param className   Simple or fully-qualified name of the invoking class.
 * @param methodName  Name of the method that was traced.
 * @param timestamp   Epoch millis when the event was captured.
 */
public record CallChainEvent(
        String id,
        String patternType,
        String className,
        String methodName,
        long timestamp
) {}
