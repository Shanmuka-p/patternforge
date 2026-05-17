package com.example.patternforge.patterns.proxy;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class AuditService {
    private static final List<String> auditLog = Collections.synchronizedList(new ArrayList<>());

    public static void log(String message) {
        auditLog.add(message);
    }

    public static List<String> getAuditLog() {
        return new ArrayList<>(auditLog);
    }
    
    public static void clearLog() {
        auditLog.clear();
    }
}
