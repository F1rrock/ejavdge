package org.ejavdge.web.resource;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

public final class PortOfTest extends TestCase {
    public void testExplicitHttpPort() {
        assertEquals(
            90,
            new PortOf(
                new Text.Of("http://localhost:90/ejudge")
            ).value()
        );
    }

    public void testExplicitHttpsPort() {
        assertEquals(
            8443,
            new PortOf(
                new Text.Of("https://example.com:8443/path")
            ).value()
        );
    }

    public void testHttpDefaultPort() {
        assertEquals(
            80,
            new PortOf(
                new Text.Of("http://localhost/")
            ).value()
        );
    }

    public void testHttpsDefaultPort() {
        assertEquals(
            443,
            new PortOf(
                new Text.Of("https://google.com/")
            ).value()
        );
    }

    public void testHttpDefaultWithoutPath() {
        assertEquals(
            80,
            new PortOf(
                new Text.Of("http://localhost")
            ).value()
        );
    }

    public void testPortWithQuery() {
        assertEquals(
            90,
            new PortOf(
                new Text.Of("http://localhost:90/ejudge?SID=abc&action=2")
            ).value()
        );
    }

    public void testPortWithUserInfo() {
        assertEquals(
            8080,
            new PortOf(
                new Text.Of("https://user:pass@example.com:8080/path")
            ).value()
        );
    }

    public void testUnknownSchemeFallsBackToEighty() {
        assertEquals(
            80,
            new PortOf(
                new Text.Of("ftp://example.com/file.txt")
            ).value()
        );
    }

    public void testMalformedUrl() {
        try {
            new PortOf(new Text.Of("ht tp://bad url")).value();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testEmptyUrl() {
        try {
            new PortOf(new Text.Of("")).value();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }
}
