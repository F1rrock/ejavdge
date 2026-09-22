package org.ejavdge.web.resource;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

public final class HostOfTest extends TestCase {
    public void testHostFromAbsoluteUrl() {
        assertEquals(
            "google.com",
            new HostOf(new Text.Of("https://google.com")).content()
        );
    }

    public void testHostWithPort() {
        assertEquals(
            "localhost",
            new HostOf(new Text.Of("http://localhost:90/ejudge")).content()
        );
    }

    public void testHostWithPath() {
        assertEquals(
            "ejudge.example.com",
            new HostOf(
                new Text.Of("https://ejudge.example.com/cgi-bin/new-client")
            ).content()
        );
    }

    public void testHostWithQuery() {
        assertEquals(
            "localhost",
            new HostOf(
                new Text.Of("http://localhost:90/ejudge?SID=abc&action=2")
            ).content()
        );
    }

    public void testHostWithUserInfo() {
        assertEquals(
            "example.com",
            new HostOf(
                new Text.Of("https://user:pass@example.com/path")
            ).content()
        );
    }

    public void testRelativeUrl() {
        try {
            new HostOf(new Text.Of("/ejudge/file.txt")).content();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testOpaqueUrl() {
        try {
            new HostOf(new Text.Of("mailto:foo@bar.com")).content();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testMalformedUrl() {
        try {
            new HostOf(new Text.Of("ht tp://bad url")).content();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testEmptyUrl() {
        try {
            new HostOf(new Text.Of("")).content();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }
}
