package com.gathin.tvhome;

import java.util.Set;

/** Tracks all foreground/background transitions, not only the stock launcher's. */
final class HomeForeground {
    private String pkg;
    private String cls;
    private long since;
    private boolean attempted;

    void foreground(String packageName, String className, long timestamp) {
        pkg = packageName;
        cls = className;
        since = timestamp;
        attempted = false;
    }

    void background(String packageName, String className) {
        if (pkg != null && pkg.equals(packageName) && (cls == null || cls.equals(className))) clear();
    }

    void clear() {
        pkg = null;
        cls = null;
        since = 0;
        attempted = false;
    }

    boolean takeHome(String stockPackage, Set<String> homes, long now) {
        if (attempted || !HomeRedirectPolicy.matches(stockPackage, homes, pkg, cls)
                || now < since || now - since < 6000 || now - since > 120000) return false;
        attempted = true;
        return true;
    }
}
