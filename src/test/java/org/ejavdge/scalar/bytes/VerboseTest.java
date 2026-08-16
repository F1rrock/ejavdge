package org.ejavdge.scalar.bytes;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;
import org.slf4j.Logger;

import java.lang.reflect.Proxy;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class VerboseTest extends TestCase {
    public void testPrintsMessageWhenCalled() {
        final List<String> calls = new ArrayList<>();
        new Verbose(
            new Bytes.Of("Hello".getBytes(StandardCharsets.UTF_8)),
            new Text.Of("Fetching session..."),
            logger(calls)
        ).content();
        assertEquals(
            Arrays.asList("debug", "Fetching session..."),
            calls
        );
    }

    public void testPreservesWrappedResult() {
        assertEquals(
            "Hello",
            new String(
                new Verbose(
                    new Bytes.Of("Hello".getBytes(StandardCharsets.UTF_8)),
                    new Text.Of("Fetching session..."),
                    logger(new ArrayList<>())
                ).content(),
                StandardCharsets.UTF_8
            )
        );
    }

    public void testPreservesWrappedException() {
        final InvariantViolation expected = new InvariantViolation("original");
        try {
            new Verbose(
                () -> {
                    throw expected;
                },
                new Text.Of("Fetching session..."),
                logger(new ArrayList<>())
            ).content();
        } catch (final InvariantViolation actual) {
            assertEquals(expected, actual);
            return;
        }
        fail("InvariantViolation");
    }

    private static Logger logger(final List<String> calls) {
        return (Logger) Proxy.newProxyInstance(
            Logger.class.getClassLoader(),
            new Class<?>[]{Logger.class},
            (proxy, method, args) -> {
                if (method.getName().equals("isDebugEnabled")) {
                    return true;
                }
                if (method.getName().equals("debug") && args.length == 1) {
                    calls.add("debug");
                    calls.add((String) args[0]);
                }
                return null;
            }
        );
    }
}