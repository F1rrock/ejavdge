package org.ejavdge.web.resource;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

public final class LastSegmentOfTest extends TestCase {
    public void testSimpleFileName() {
        assertEquals(
            "sample.txt",
            new LastSegmentOf(
                new PathOf(
                    new Text.Of("http://localhost:90/files/sample.txt")
                )
            ).content()
        );
    }

    public void testNestedPath() {
        assertEquals(
            "file.txt",
            new LastSegmentOf(
                new PathOf(
                    new Text.Of("http://localhost:90/a/b/c/file.txt")
                )
            ).content()
        );
    }

    public void testPathWithQuery() {
        assertEquals(
            "sample.txt",
            new LastSegmentOf(
                new PathOf(
                    new Text.Of("http://localhost:90/files/sample.txt?id=42")
                )
            ).content()
        );
    }

    public void testPathWithFragment() {
        assertEquals(
            "page",
            new LastSegmentOf(
                new PathOf(
                    new Text.Of("http://localhost:90/docs/page#section")
                )
            ).content()
        );
    }

    public void testTrailingSlash() {
        assertEquals(
            "",
            new LastSegmentOf(
                new PathOf(
                    new Text.Of("http://localhost:90/files/")
                )
            ).content()
        );
    }

    public void testRootPath() {
        assertEquals(
            "",
            new LastSegmentOf(
                new PathOf(
                    new Text.Of("http://localhost:90/")
                )
            ).content()
        );
    }

    public void testEmptyPath() {
        assertEquals(
            "",
            new LastSegmentOf(
                new PathOf(
                    new Text.Of("http://localhost:90")
                )
            ).content()
        );
    }

    public void testMalformedUrl() {
        try {
            new LastSegmentOf(
                new PathOf(new Text.Of("ht tp://bad url"))
            ).content();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testOpaqueUrl() {
        try {
            new LastSegmentOf(
                new PathOf(new Text.Of("mailto:foo@bar.com"))
            ).content();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }
}
