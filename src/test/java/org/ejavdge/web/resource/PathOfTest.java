package org.ejavdge.web.resource;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

public final class PathOfTest extends TestCase {
    public void testPathFromAbsoluteUrl() {
        assertEquals(
            "/ejudge/file.txt",
            new PathOf(
                new Text.Of("http://localhost:90/ejudge/file.txt")
            ).content()
        );
    }

    public void testRootPath() {
        assertEquals(
            "/",
            new PathOf(new Text.Of("https://google.com/")).content()
        );
    }

    public void testEmptyPath() {
        assertEquals(
            "/",
            new PathOf(new Text.Of("https://google.com")).content()
        );
    }

    public void testPathWithQuery() {
        assertEquals(
            "/ejudge",
            new PathOf(
                new Text.Of("http://localhost:90/ejudge?SID=abc&action=2")
            ).content()
        );
    }

    public void testPathWithFragment() {
        assertEquals(
            "/page",
            new PathOf(
                new Text.Of("https://example.com/page#section")
            ).content()
        );
    }

    public void testNestedPath() {
        assertEquals(
            "/cgi-bin/new-client",
            new PathOf(
                new Text.Of("https://ejudge.example.com/cgi-bin/new-client")
            ).content()
        );
    }

    public void testPathWithUserInfo() {
        assertEquals(
            "/path",
            new PathOf(
                new Text.Of("https://user:pass@example.com/path")
            ).content()
        );
    }

    public void testOpaqueUrl() {
        try {
            new PathOf(new Text.Of("mailto:foo@bar.com")).content();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testMalformedUrl() {
        try {
            new PathOf(new Text.Of("ht tp://bad url")).content();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testEmptyUrl() {
        try {
            new PathOf(new Text.Of("")).content();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }
}
