package org.ejavdge.web.resource;

import junit.framework.TestCase;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.web.context.FakeContext;
import org.ejavdge.web.context.NoContext;
import org.ejavdge.web.context.WithEntry;

public final class UrlTest extends TestCase {
    public void testBaseOnly() {
        assertEquals(
            "https://example.com/path",
            new Url(
                new Text.Of("https://example.com/path")
            ).content()
        );
    }

    public void testEscapedQuery() {
        assertEquals(
            "https://example.com?name%201=value%201&name%202=value%202",
            new Url(
                new Url(
                    new Text.Of("https://example.com")
                ),
                new FakeContext()
            ).content()
        );
    }

    public void testEmptyQuery() {
        assertEquals(
            "https://example.com",
            new Url(
                new Url(
                    new Text.Of("https://example.com")
                ),
                new NoContext()
            ).content()
        );
    }

    public void testQueryCompose() {
        assertEquals(
            "https://example.com?name1=value1&name2=value2",
            new Url(
                new Url(
                    new Url(
                        new Text.Of("https://example.com")
                    ),
                    new WithEntry(
                        new Text.Of("name1"),
                        new Text.Of("value1")
                    )
                ),
                new WithEntry(
                    new Text.Of("name2"),
                    new Text.Of("value2")
                )
            ).content()
        );
    }
}
