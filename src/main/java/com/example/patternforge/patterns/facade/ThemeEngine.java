package com.example.patternforge.patterns.facade;

import org.springframework.stereotype.Service;

@Service
public class ThemeEngine {
    public String applyColors() {
        return "Colors applied";
    }
}
