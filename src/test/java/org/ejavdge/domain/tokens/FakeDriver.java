package org.ejavdge.domain.tokens;

import org.ejavdge.web.context.Location;
import org.ejavdge.web.driver.WebDriver;
import org.ejavdge.web.media.FakeMedia;
import org.ejavdge.web.spec.Request;

import java.nio.charset.StandardCharsets;

public final class FakeDriver implements WebDriver {
    @Override
    public byte[] resourceOf(final Location loc, final Request req) {
        final boolean valid = loc.imprint(new FakeMedia())
            .equals("url:/ejudge:host:0.0.0.0:port:90:");
        return (valid ?
            """
            HTTP/1.1 302 Found\r
            Date: Sun, 02 Aug 2026 18:37:12 GMT\r
            Server: Apache/2.4.62 (Fedora Linux)\r
            Set-Cookie: EJSID=306024e6e473944c; Path=/; SameSite=Lax\r
            Location: http://0.0.0.0:90/ejudge?SID=83fc1f5383917944&action=2&lt=1\r
            Content-Length: 251\r
            Content-Type: text/html; charset=iso-8859-1\r \s
            \r
            <!DOCTYPE HTML PUBLIC "-//IETF//DTD HTML 2.0//EN">
            <html><head>
            <title>302 Found</title>
            </head><body>
            <h1>Found</h1>
            <p>The document has moved <a href="http://0.0.0.0:90/ejudge?SID=83fc1f5383917944&amp;action=2&amp;lt=1">here</a>.</p>   \s
            </body></html>
           \s""" : "no contents").getBytes(StandardCharsets.UTF_8);
    }
}
