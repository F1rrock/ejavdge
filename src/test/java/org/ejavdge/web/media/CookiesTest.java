package org.ejavdge.web.media;

import junit.framework.TestCase;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.web.context.FakeContext;

public final class CookiesTest extends TestCase {
    public void testEmptyCookies() {
        final var cookies = new Cookies();
        assertEquals("", cookies.content());
    }

    public void testSingleCookie() {
        final var cookies = new Cookies()
            .with(new Text.Of("SID"), new Text.Of("123abc"));
        assertEquals("SID=123abc", cookies.content());
    }

    public void testMultipleCookies() {
        final var cookies = new Cookies()
            .with(new Text.Of("SID"), new Text.Of("123abc"))
            .with(new Text.Of("EJSID"), new Text.Of("456def"));
        assertEquals("SID=123abc; EJSID=456def", cookies.content());
    }

    public void testCookieWithSpecialCharacters() {
        final var cookies = new Cookies()
            .with(new Text.Of("path"), new Text.Of("/ejudge"))
            .with(new Text.Of("value"), new Text.Of("a=b&c=d"));
        assertEquals("path=%2Fejudge; value=a%3Db%26c%3Dd", cookies.content());
    }

    public void testCookieWithSpaces() {
        final var cookies = new Cookies()
            .with(new Text.Of("name"), new Text.Of("John Doe"));
        assertEquals("name=John%20Doe", cookies.content());
    }

    public void testCookieWithEmptyValue() {
        final var cookies = new Cookies()
            .with(new Text.Of("empty"), new Text.Of(""));
        assertEquals("empty=", cookies.content());
    }

    public void testImprintOf() {
        assertEquals(
            "name%201=value%201; name%202=value%202",
            new Cookies.ImprintOf(
                new FakeContext()
            ).content()
        );
    }

    public void testChainedCookies() {
        final var cookies = new Cookies()
            .with(new Text.Of("a"), new Text.Of("1"))
            .with(new Text.Of("b"), new Text.Of("2"))
            .with(new Text.Of("c"), new Text.Of("3"));
        assertEquals("a=1; b=2; c=3", cookies.content());
    }
}