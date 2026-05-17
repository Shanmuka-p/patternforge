package com.example.patternforge.patterns.facade;

import org.springframework.stereotype.Service;

@Service
public class RenderEngine {
    public String flushBuffer() {
        return "Buffer flushed";
    }
}
