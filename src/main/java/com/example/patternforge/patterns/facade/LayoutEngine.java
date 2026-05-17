package com.example.patternforge.patterns.facade;

import org.springframework.stereotype.Service;

@Service
public class LayoutEngine {
    public String calculateGrid() {
        return "Grid calculated";
    }
}
