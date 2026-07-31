package org.ejavdge.web.spec.method;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.scalar.text.Empty;
import org.ejavdge.scalar.text.Text;

import java.nio.charset.StandardCharsets;

public final class PostTest extends TestCase {
    public void testRequest() {
        assertEquals(
            """
            POST /ejudge HTTP/1.1\r
            Host: example.com:80\r
            """,
            new String(
                new Post(
                    new Text.Of("/ejudge"),
                    new Text.Of("example.com"),
                    new Num.Of(80)
                ).bytes(),
                StandardCharsets.US_ASCII
            )
        );
    }

    public void testEmptyUrl() {
        try {
            new Post(
                new Empty(),
                new Text.Of("example.com"),
                new Num.Of(80)
            ).bytes();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testEmptyHost() {
        try {
            new Post(
                new Text.Of("/ejudge"),
                new Empty(),
                new Num.Of(80)
            ).bytes();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testNonPositivePort() {
        try {
            new Post(
                new Text.Of("/ejudge"),
                new Text.Of("example.com"),
                new Num.Of(-1)
            ).bytes();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }
}
