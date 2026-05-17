package com.example.patternforge.patterns.proxy;

import com.example.patternforge.domain.DashboardComponent;
import com.example.patternforge.domain.RenderResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProxyPatternTest {

    @BeforeEach
    void setUp() {
        AuditService.clearLog();
        SessionState.setCurrentRole("GUEST");
    }

    @Test
    void testLazyWidgetProxy() {
        LazyWidgetProxy proxy = new LazyWidgetProxy("vid1");
        assertEquals("UNLOADED", proxy.getState());
        
        RenderResult skeletonResult = proxy.render();
        assertTrue(skeletonResult.html().contains("skeleton"));
        
        proxy.loadData();
        assertEquals("LOADED", proxy.getState());
        
        RenderResult loadedResult = proxy.render();
        assertTrue(loadedResult.html().contains("video"));
    }

    @Test
    void testAccessControlProxy() {
        DashboardComponent dummyWidget = new DashboardComponent() {
            @Override public String getId() { return "d1"; }
            @Override public RenderResult render() { return new RenderResult("<div>Secret Content</div>", List.of()); }
            @Override public String getPatternInfo() { return ""; }
        };

        AccessControlProxy proxy = new AccessControlProxy(dummyWidget, "ADMIN");
        
        // As GUEST
        RenderResult deniedResult = proxy.render();
        assertTrue(deniedResult.html().contains("Access Denied"));
        assertFalse(deniedResult.html().contains("Secret Content"));

        // As ADMIN
        SessionState.setCurrentRole("ADMIN");
        RenderResult allowedResult = proxy.render();
        assertTrue(allowedResult.html().contains("Secret Content"));
    }

    @Test
    void testAuditWidgetProxy() {
        DashboardComponent dummyWidget = new DashboardComponent() {
            @Override public String getId() { return "d1"; }
            @Override public RenderResult render() { return new RenderResult("<div>Audited Content</div>", List.of()); }
            @Override public String getPatternInfo() { return ""; }
        };

        AuditWidgetProxy proxy = new AuditWidgetProxy(dummyWidget);
        proxy.render();
        
        List<String> log = AuditService.getAuditLog();
        assertEquals(1, log.size());
        assertTrue(log.get(0).contains("Widget rendered at"));
    }
}
