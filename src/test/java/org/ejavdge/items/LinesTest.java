package org.ejavdge.items;

import junit.framework.TestCase;
import org.ejavdge.scalar.text.Text;

import java.util.List;

public final class LinesTest extends TestCase {
    public void testDefaultSeparator() {
        assertEquals(
            List.of("line1", "line2", "line3"),
            new Lines(
                new Text.Of("line1\nline2\nline3")
            ).contents()
                .stream()
                .map(Text::content)
                .toList()
        );
    }

    public void testLineCount() {
        final List<Text> lines = new Lines(
            new Text.Of("line1\nline2\nline3")
        ).contents();
        assertEquals(3, lines.size());
    }

    public void testCustomSeparator() {
        assertEquals(
            List.of("a", "b", "c"),
            new Lines(
                new Text.Of(";"),
                new Text.Of("a;b;c")
            ).contents()
                .stream()
                .map(Text::content)
                .toList()
        );
    }

    public void testEmptyText() {
        assertEquals(
            List.of(""),
            new Lines(
                new Text.Of("")
            ).contents()
                .stream()
                .map(Text::content)
                .toList()
        );
    }

    public void testWithoutSeparator() {
        assertEquals(
            List.of("lines"),
            new Lines(
                new Text.Of("lines")
            ).contents()
                .stream()
                .map(Text::content)
                .toList()
        );
    }

    public void testEmptySeparator() {
        assertEquals(
            List.of("a", "b", "c"),
            new Lines(
                new Text.Of(""),
                new Text.Of("abc")
            ).contents()
                .stream()
                .map(Text::content)
                .toList()
        );
    }

    public void testTrailingNewline() {
        assertEquals(
            List.of("line1", "line2"),
            new Lines(
                new Text.Of("line1\nline2\n\n")
            ).contents()
                .stream()
                .map(Text::content)
                .toList()
        );
    }
}
