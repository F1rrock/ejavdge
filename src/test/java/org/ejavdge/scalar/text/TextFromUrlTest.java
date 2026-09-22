package org.ejavdge.scalar.text;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;

public final class TextFromUrlTest extends TestCase {
    public void testPlainTextUnchanged() {
        assertEquals(
            "sample.txt",
            new TextFromUrl(new Text.Of("sample.txt")).content()
        );
    }

    public void testDecodesPercentTwenty() {
        assertEquals(
            "my file.txt",
            new TextFromUrl(new Text.Of("my%20file.txt")).content()
        );
    }

    public void testDecodesPlusAsSpace() {
        assertEquals(
            "my file.txt",
            new TextFromUrl(new Text.Of("my+file.txt")).content()
        );
    }

    public void testDecodesCyrillic() {
        assertEquals(
            "пример.txt",
            new TextFromUrl(
                new Text.Of(
                    "%D0%BF%D1%80%D0%B8%D0%BC%D0%B5%D1%80.txt"
                )
            ).content()
        );
    }

    public void testDecodesMixed() {
        assertEquals(
            "a b+пример",
            new TextFromUrl(
                new Text.Of("a%20b%2B%D0%BF%D1%80%D0%B8%D0%BC%D0%B5%D1%80")
            ).content()
        );
    }

    public void testDecodesEncodedPlus() {
        assertEquals(
            "a+b",
            new TextFromUrl(new Text.Of("a%2Bb")).content()
        );
    }

    public void testEmptyUrl() {
        assertEquals(
            "",
            new TextFromUrl(new Text.Of("")).content()
        );
    }

    public void testMalformedDefaultMessage() {
        try {
            new TextFromUrl(new Text.Of("%G1")).content();
        } catch (final InvariantViolation e) {
            assertTrue(
                e.getMessage().contains("There is no legal url.")
            );
            return;
        }
        fail("InvariantViolation");
    }

    public void testMalformedCustomMessage() {
        try {
            new TextFromUrl(
                new Text.Of("%G1"),
                new Text.Of("Bad url in attachment.")
            ).content();
        } catch (final InvariantViolation e) {
            assertTrue(
                e.getMessage().contains("Bad url in attachment.")
            );
            return;
        }
        fail("InvariantViolation");
    }

    public void testIncompletePercent() {
        try {
            new TextFromUrl(new Text.Of("%2")).content();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testBarePercent() {
        try {
            new TextFromUrl(new Text.Of("%")).content();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }
}
