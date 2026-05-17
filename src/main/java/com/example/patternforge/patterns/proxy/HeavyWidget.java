package com.example.patternforge.patterns.proxy;

import com.example.patternforge.domain.DashboardComponent;

public interface HeavyWidget extends DashboardComponent {
    void loadData();
}
