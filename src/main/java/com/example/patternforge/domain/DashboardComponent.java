package com.example.patternforge.domain;

import java.util.List;

public interface DashboardComponent {

    String getId();

    RenderResult render();

    String getPatternInfo();

    default void add(DashboardComponent child) {
        throw new UnsupportedOperationException("Leaf nodes cannot have children");
    }

    default void remove(DashboardComponent child) {
        throw new UnsupportedOperationException("Leaf nodes cannot have children");
    }

    default List<DashboardComponent> getChildren() {
        throw new UnsupportedOperationException("Leaf nodes cannot have children");
    }
}
