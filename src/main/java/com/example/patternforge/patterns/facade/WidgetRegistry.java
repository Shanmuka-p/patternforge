package com.example.patternforge.patterns.facade;

import org.springframework.stereotype.Service;

@Service
public class WidgetRegistry {
    public String registerDefaults() {
        return "Defaults registered";
    }
}
