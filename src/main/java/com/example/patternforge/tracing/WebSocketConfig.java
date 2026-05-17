package com.example.patternforge.tracing;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * Configures the STOMP-over-WebSocket infrastructure used by PatternForge to
 * stream call-chain events to connected front-end clients in real time.
 *
 * <p>Endpoint  : /ws-patternforge (SockJS fallback enabled)</p>
 * <p>Broker    : simple in-memory broker on /topic</p>
 * <p>App prefix: /app</p>
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // Enable a simple in-memory broker; clients subscribe to /topic/**
        registry.enableSimpleBroker("/topic");
        // Prefix for messages routed to @MessageMapping methods
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry
                .addEndpoint("/ws-patternforge")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }
}
