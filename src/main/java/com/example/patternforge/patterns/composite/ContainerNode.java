package com.example.patternforge.patterns.composite;

import com.example.patternforge.domain.DashboardComponent;
import com.example.patternforge.domain.RenderResult;
import com.example.patternforge.tracing.PatternStackTracer;

import java.util.ArrayList;
import java.util.List;

public class ContainerNode implements DashboardComponent {
    private final String id;
    private final List<DashboardComponent> children = new ArrayList<>();
    private ContainerNode parent;

    public ContainerNode(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    public ContainerNode getParent() {
        return parent;
    }

    public void setParent(ContainerNode parent) {
        this.parent = parent;
    }

    @Override
    public RenderResult render() {
        PatternStackTracer.trace("Composite", "ContainerNode", "render");
        StringBuilder htmlBuilder = new StringBuilder();
        List<String> combinedStyles = new ArrayList<>();

        htmlBuilder.append("<div id=\"").append(id).append("\">");
        for (DashboardComponent child : children) {
            RenderResult childResult = child.render();
            htmlBuilder.append(childResult.html());
            combinedStyles.addAll(childResult.cssStyles());
        }
        htmlBuilder.append("</div>");

        return new RenderResult(htmlBuilder.toString(), combinedStyles);
    }

    @Override
    public String getPatternInfo() {
        return "Composite Pattern: Composite node";
    }

    @Override
    public void add(DashboardComponent child) {
        if (this.id.equals(child.getId())) {
            throw new IllegalArgumentException("Cannot add container to itself.");
        }

        if (child instanceof ContainerNode childContainer) {
            ContainerNode currentAncestor = this;
            while (currentAncestor != null) {
                if (currentAncestor.getId().equals(child.getId())) {
                    throw new IllegalArgumentException("Circular reference detected.");
                }
                currentAncestor = currentAncestor.getParent();
            }
            childContainer.setParent(this);
        }

        this.children.add(child);
    }

    @Override
    public void remove(DashboardComponent child) {
        this.children.remove(child);
        if (child instanceof ContainerNode childContainer && childContainer.getParent() == this) {
            childContainer.setParent(null);
        }
    }

    @Override
    public List<DashboardComponent> getChildren() {
        return new ArrayList<>(this.children);
    }
}
