package org.ejavdge.domain.solution;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.web.context.Location;
import org.ejavdge.web.driver.WebDriver;
import org.ejavdge.web.spec.Request;

import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

public final class FakeDriver implements WebDriver {
    private final String expected;
    private final String boundary;

    public FakeDriver(final String s) {
        this(s, "{BOUNDARY}");
    }

    public FakeDriver(final String s, final String b) {
        this.expected = s;
        this.boundary = b;
    }

    @Override
    public byte[] resourceOf(final Location loc, final Request req) {
        final var actual = new String(
            req.bytes(),
            StandardCharsets.UTF_8
        );
        final var match = Pattern
            .compile(
                "boundary\\s*=\\s*(----WebKitFormBoundary[0-9a-fA-F-]+)"
            )
            .matcher(actual);
        if (!match.find()) {
            throw new InvariantViolation("there is no valid resource");
        }
        if (this.expected.replace(this.boundary, match.group(1)).equals(actual)) {
            return """
                    HTTP/1.1 302 FOUND\r
                    Host: localhost:90\r
                    Content-Length: 5\r
                    \r
                    Sent!\r
                    """.getBytes(StandardCharsets.UTF_8);
        } else {
            return """
                    HTTP/1.1 200 OK\r
                    Host: localhost:90\r
                    Content-Length: 18\r
                    \r
                    Invalid submission\r
                    """.getBytes(StandardCharsets.UTF_8);
        }
    }
}
