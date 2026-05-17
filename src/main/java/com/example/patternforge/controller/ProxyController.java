package com.example.patternforge.controller;

import com.example.patternforge.patterns.proxy.AuditService;
import com.example.patternforge.patterns.proxy.LazyWidgetProxy;
import com.example.patternforge.patterns.proxy.SessionState;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
public class ProxyController {

    private final Map<String, LazyWidgetProxy> proxyMap = new ConcurrentHashMap<>();

    @PostMapping("/api/widget/{id}/load")
    public String loadWidget(@PathVariable String id) {
        LazyWidgetProxy proxy = proxyMap.computeIfAbsent(id, k -> new LazyWidgetProxy(k));
        proxy.loadData();
        return proxy.getState();
    }

    @PutMapping("/api/session/role")
    public String updateRole(@RequestParam String role) {
        SessionState.setCurrentRole(role);
        return "Role updated to: " + role;
    }

    @GetMapping("/api/audit-log")
    public List<String> getAuditLog() {
        return AuditService.getAuditLog();
    }
}
