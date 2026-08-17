package org.ejavdge.scalar.bytes;

import org.slf4j.Logger;
import org.slf4j.Marker;

public final class FakeLogger implements Logger {
    public boolean isDebugEnabledCalled = false;
    public int debugCalls = 0;
    public String lastMessageTemplate = null;
    private boolean enabled = true;

    public void setEnabled(boolean enabled) { this.enabled = enabled; }

    @Override public String getName() {
        return "";
    }

    @Override
    public boolean isDebugEnabled() {
        isDebugEnabledCalled = true;
        return enabled;
    }

    @Override
    public void debug(String msg) {
        debugCalls++;
        lastMessageTemplate = msg;
    }

    public boolean written() {
        return debugCalls > 0;
    }

    public String cache() {
        return lastMessageTemplate;
    }


    private void forbidden(String method) {
        throw new AssertionError(
            "FakeLogger: forbidden method: " + method + "."
        );
    }

    @Override public void trace(String msg) { forbidden("trace(String)"); }
    @Override public void trace(String s, Object ...args) { forbidden("trace(String, ...Object)"); }
    @Override public void trace(String msg, Object o) { forbidden("trace(String, Object)"); }
    @Override public void trace(String s, Object o, Object o1) { forbidden("trace(String, Object, Object)"); }
    @Override public void debug(String s, Object o) { forbidden("debug(String, Object)"); }
    @Override public void debug(String s, Object o, Object o1) { forbidden("debug(String, Object, Object)");}
    @Override public void debug(String s, Object... objects) { forbidden("debug(String, Object...)"); }
    @Override public void info(String msg) { forbidden("info"); }
    @Override public void info(String s, Object o) { forbidden("info(String, Object)"); }
    @Override public void info(String s, Object o, Object o1) { forbidden("info(String, Object, Object)"); }
    @Override public void info(String s, Object... objects) { forbidden("info(String, Object...)");}
    @Override public void warn(String msg) { forbidden("warn"); }
    @Override public void warn(String s, Object o) { forbidden("warn(String, Object)"); }
    @Override public void warn(String s, Object... objects) { forbidden("warn(String, Object...)"); }
    @Override public void warn(String s, Object o, Object o1) { forbidden("warn(String, Object, Object)"); }
    @Override public void error(String msg) { forbidden("error"); }
    @Override public void error(String s, Object o) { forbidden("error(String, Object)"); }
    @Override public void error(String s, Object o, Object o1) { forbidden("error(String, Object, Object)"); }
    @Override public void error(String s, Object... objects) { forbidden("error(String, Object...)"); }
    @Override public void trace(String msg, Throwable t) { forbidden("trace(String, Throwable)"); }
    @Override public boolean isTraceEnabled(Marker marker) {
        forbidden("isTraceEnabled(Marker)");
        return false;
    }
    @Override public void debug(String msg, Throwable t) { forbidden("debug(String, Throwable)"); }
    @Override public boolean isDebugEnabled(Marker marker) {
        forbidden("isDebugEnabled(Marker)");
        return false;
    }

    @Override public void info(String msg, Throwable t) { forbidden("info(String, Throwable)"); }
    @Override public boolean isInfoEnabled(Marker marker) {
        forbidden("isInfoEnabled(Marker)");
        return false;
    }
    @Override public void warn(String msg, Throwable t) { forbidden("warn(String, Throwable)"); }
    @Override public boolean isWarnEnabled(Marker marker) {
        forbidden("isWarnEnabled(Marker)");
        return false;
    }
    @Override public void error(String msg, Throwable t) { forbidden("error(String, Throwable)"); }
    @Override public boolean isErrorEnabled(Marker marker) {
        forbidden("isErrorEnabled(Marker)");
        return false;
    }
    @Override public void trace(Marker marker, String msg) { forbidden("trace(Marker, String)"); }
    @Override public void trace(Marker marker, String s, Object o) { forbidden("trace(Marker, String, Object)"); }
    @Override public void trace(Marker marker, String s, Object o, Object o1) { forbidden("trace(Marker, String, Object, Object)"); }
    @Override public void trace(Marker marker, String s, Object... objects) { forbidden("trace(Marker, String, Object...)"); }
    @Override public void debug(Marker marker, String msg) { forbidden("debug(Marker, String)"); }
    @Override public void debug(Marker marker, String s, Object o) { forbidden("debug(Marker, String, Object)"); }
    @Override public void debug(Marker marker, String s, Object o, Object o1) { forbidden("debug(Marker, String, Object, Object)"); }
    @Override public void debug(Marker marker, String s, Object... objects) { forbidden("debug(Marker, String, Object...)"); }
    @Override public void info(Marker marker, String msg) { forbidden("info(Marker, String)"); }
    @Override public void info(Marker marker, String s, Object o) { forbidden("info(Marker, String, Object)"); }
    @Override public void info(Marker marker, String s, Object o, Object o1) { forbidden("info(Marker, String, Object, Object)"); }
    @Override public void info(Marker marker, String s, Object... objects) { forbidden("info(Marker, String, Object...)"); }
    @Override public void warn(Marker marker, String msg) { forbidden("warn(Marker, String)"); }
    @Override public void warn(Marker marker, String s, Object o) { forbidden("warn(Marker, String, Object)"); }
    @Override public void warn(Marker marker, String s, Object o, Object o1) { forbidden("warn(Marker, String, Object, Object)"); }
    @Override public void warn(Marker marker, String s, Object... objects) { forbidden("warn(Marker, String, Object...)"); }
    @Override public void error(Marker marker, String msg) { forbidden("error(Marker, String)"); }
    @Override public void error(Marker marker, String s, Object o) { forbidden("error(Marker, String, Object)"); }
    @Override public void error(Marker marker, String s, Object o, Object o1) { forbidden("error(Marker, String, Object, Object)"); }
    @Override public void error(Marker marker, String s, Object... objects) { forbidden("error(Marker, String, Object...)"); }
    @Override public void trace(Marker marker, String msg, Throwable t) { forbidden("trace(Marker, String, Throwable)"); }
    @Override public void debug(Marker marker, String msg, Throwable t) { forbidden("debug(Marker, String, Throwable)"); }
    @Override public void info(Marker marker, String msg, Throwable t) { forbidden("info(Marker, String, Throwable)"); }
    @Override public void warn(Marker marker, String msg, Throwable t) { forbidden("warn(Marker, String, Throwable)"); }
    @Override public void error(Marker marker, String msg, Throwable t) { forbidden("error(Marker, String, Throwable)"); }
    @Override public boolean isTraceEnabled() { forbidden("isTraceEnabled"); return false; }
    @Override public boolean isInfoEnabled() { forbidden("isInfoEnabled"); return false; }
    @Override public boolean isWarnEnabled() { forbidden("isWarnEnabled"); return false; }
    @Override public boolean isErrorEnabled() { forbidden("isErrorEnabled"); return false; }
}
