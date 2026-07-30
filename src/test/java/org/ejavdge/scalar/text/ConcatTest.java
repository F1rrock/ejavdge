package org.ejavdge.scalar.text;

import junit.framework.TestCase;
import org.ejavdge.items.Items;

public final class ConcatTest extends TestCase {
    public void testWithoutSeparator() {
        assertEquals(
            "HelloWorld",
            new Concat(
                new Text.Of("Hello"),
                new Text.Of("World")
            ).content()
        );
    }

    public void testWithSeparator() {
        assertEquals(
            "Hello, World",
            new Concat(
                new Text.Of(", "),
                new Items.Of<>(
                    new Text.Of("Hello"),
                    new Text.Of("World")
                )
            ).content()
        );
    }

    public void testSingleText() {
        assertEquals(
            "Hello",
            new Concat(
                new Text.Of(", "),
                new Items.Of<>(
                    new Text.Of("Hello")
                )
            ).content()
        );
    }

    public void testEmpty() {
        assertEquals(
            "",
            new Concat(
                new Text.Of(", "),
                new Items.Of<>()
            ).content()
        );
    }

    public void testStrings() {
        assertEquals(
            "HelloWorld",
            new Concat(
                "Hello",
                "World"
            ).content()
        );
    }
}
