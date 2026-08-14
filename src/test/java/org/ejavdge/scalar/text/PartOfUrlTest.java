package org.ejavdge.scalar.text;

import junit.framework.TestCase;

public final class PartOfUrlTest extends TestCase {
    public void testSpace() {
        assertEquals(
            "Log%20in",
            new PartOfUrl(
                new Text.Of("Log in")
            ).content()
        );
    }

    public void testSpecialCharacters() {
        assertEquals(
            "a%26b%3Dc",
            new PartOfUrl(
                new Text.Of("a&b=c")
            ).content()
        );
    }

    public void testUnicode() {
        assertEquals(
            "%D0%BF%D1%80%D0%B8%D0%B2%D0%B5%D1%82",
            new PartOfUrl(
                new Text.Of("привет")
            ).content()
        );
    }

    public void testPlainText() throws Exception {
        assertEquals(
            "vader",
            new PartOfUrl(
                new Text.Of("vader")
            ).content()
        );
    }
}
