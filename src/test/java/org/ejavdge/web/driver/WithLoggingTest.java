package org.ejavdge.web.driver;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.scalar.text.Utf8Text;
import org.ejavdge.web.context.Location;
import org.ejavdge.web.spec.HttpSpec;
import org.ejavdge.web.spec.Request;

public final class WithLoggingTest extends TestCase {
    public void testWithLog() {
        final var log = new FakeLogger();
        log.setEnabled(true);
        new WithLogging(
            (loc, req) -> (
                "Response to: " + new Utf8Text(
                    new Bytes.Of(req.bytes())
                ).content()
            ).getBytes(),
            log
        ).resourceOf(
            new Location(
                new Text.Of("/test"),
                new Text.Of("localhost"),
                new Num.Of(8080)
            ),
            new Request(
                new HttpSpec.Of(
                    """
                    GET /test HTTP/1.1\r
                    Host: localhost\r
                    """
                        .getBytes()
                )
            )
        );
        assertEquals(
            """
            GET /test HTTP/1.1\r
            Host: localhost\r
            \r
            """,
            log.cache()
        );
    }

    public void testWithoutLog() {
        final var log = new FakeLogger();
        log.setEnabled(false);
        new WithLogging(
            (loc, req) -> (
                "Response to: " + new Utf8Text(
                    new Bytes.Of(req.bytes())
                ).content()
            ).getBytes(),
            log
        ).resourceOf(
            new Location(
                new Text.Of("/test"),
                new Text.Of("localhost"),
                new Num.Of(8080)
            ),
            new Request(
                new HttpSpec.Of(
                    """
                    GET /test HTTP/1.1\r
                    Host: localhost\r
                    """.getBytes()
                )
            )
        );
        assertFalse(log.written());
    }

    public void testErrorPropagation() {
        try {
            final var log = new FakeLogger();
            log.setEnabled(false);
            new WithLogging(
                (loc, req) -> {
                    throw new InvariantViolation("origin failed intentionally");
                },
                log
            ).resourceOf(
                new Location(
                    new Text.Of("/"),
                    new Text.Of("example.com"),
                    new Num.Of(80)
                ),
                new Request(
                    new HttpSpec.Of(
                        """
                        GET / HTTP/1.1\r
                        Host: example.com\r
                        """.getBytes())
                )
            );
        } catch (final InvariantViolation e) {
            assertEquals("origin failed intentionally", e.getMessage());
            return;
        }
        fail("Expected InvariantViolation from origin");
    }
}
