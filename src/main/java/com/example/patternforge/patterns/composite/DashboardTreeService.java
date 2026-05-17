package com.example.patternforge.patterns.composite;

import com.example.patternforge.domain.DashboardComponent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DashboardTreeService {

    private final Map<String, DashboardComponent> nodeRegistry = new ConcurrentHashMap<>();
    private final ContainerNode root;

    public DashboardTreeService() {
        this.root = new ContainerNode("root");
        this.nodeRegistry.put("root", root);
    }

    public void registerNode(DashboardComponent node) {
        this.nodeRegistry.put(node.getId(), node);
    }

    public DashboardComponent getNode(String id) {
        return this.nodeRegistry.get(id);
    }

    public void addNodeToContainer(String containerId, String childId) {
        DashboardComponent containerComponent = this.nodeRegistry.get(containerId);
        DashboardComponent childComponent = this.nodeRegistry.get(childId);

        if (containerComponent == null || childComponent == null) {
            throw new IllegalArgumentException("Container or child node not found in registry.");
        }

        if (containerComponent instanceof ContainerNode container) {
            container.add(childComponent);
        } else {
            throw new IllegalArgumentException("Component with id '" + containerId + "' is not a valid container.");
        }
    }
}
