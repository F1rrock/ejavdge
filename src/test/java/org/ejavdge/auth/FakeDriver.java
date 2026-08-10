package org.ejavdge.auth;

import org.ejavdge.web.context.Location;
import org.ejavdge.web.driver.WebDriver;
import org.ejavdge.web.spec.Request;

import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

public final class FakeDriver implements WebDriver {
    private final Pattern credentials;

    public FakeDriver(final String login, final String password) {
        this.credentials = Pattern.compile(
            "login=" + Pattern.quote(login)
                + "&password=" + Pattern.quote(password)
        );
    }

    @Override
    public byte[] resourceOf(final Location loc, final Request req) {
        final var request = new String(
            req.bytes(),
            StandardCharsets.UTF_8
        );
        if (this.credentials.matcher(request).find()) {
            return """
                HTTP/1.1 302 Found\r
                Content-Length: 2\r
                \r
                OK
                """.getBytes(StandardCharsets.UTF_8);
        }
        return """
            HTTP/1.1 401 Unauthorized\r
            Content-Length: 6\r
            \r
            Failed
            """.getBytes(StandardCharsets.UTF_8);
    }
}
