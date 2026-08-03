package org.ejavdge.web.spec.method;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.scalar.text.Empty;
import org.ejavdge.scalar.text.Text;

import java.nio.charset.StandardCharsets;

public final class GetTest extends TestCase {
    public void testRequest() {
        assertEquals(
            """
            GET /ejudge HTTP/1.1\r
            Host: example.com:80\r
            """,
            new String(
                new Get(
                    new Text.Of("/ejudge"),
                    new Text.Of("example.com"),
                    new Num.Of(80)
                ).bytes(),
                StandardCharsets.UTF_8
            )
        );
    }

    public void testEmptyUrl() {
        try {
            new Get(
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
            new Get(
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
            new Get(
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
