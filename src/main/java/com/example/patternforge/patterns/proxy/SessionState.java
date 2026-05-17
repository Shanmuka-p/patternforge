package com.example.patternforge.patterns.proxy;

public class SessionState {
    private static String currentRole = "GUEST";

    public static String getCurrentRole() {
        return currentRole;
    }

    public static void setCurrentRole(String role) {
        currentRole = role;
    }
}
