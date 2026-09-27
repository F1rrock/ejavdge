package org.ejavdge.items;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

import java.util.List;

public final class SignificantLinesTest extends TestCase {
    public void testEmptyText() throws InvariantViolation {
        assertEquals(
            List.of(),
            new SignificantLines(new Text.Of("")).contents()
        );
    }

    public void testSingleLine() throws InvariantViolation {
        assertEquals(
            List.of("hello"),
            this.content(
                new SignificantLines(new Text.Of("hello"))
            )
        );
    }

    public void testSeveralLines() throws InvariantViolation {
        assertEquals(
            List.of("a", "b", "c"),
            this.content(
                new SignificantLines(new Text.Of("a\nb\nc"))
            )
        );
    }

    public void testTrimmingOfLines() throws InvariantViolation {
        assertEquals(
            List.of("a", "b", "c"),
            this.content(
                new SignificantLines(
                    new Text.Of("  a  \n\tb\t\n   c   ")
                )
            )
        );
    }

    public void testFilteringOfEmptyLines() throws InvariantViolation {
        assertEquals(
            List.of("a", "b", "c"),
            this.content(
                new SignificantLines(
                    new Text.Of("a\n\n\nb\n\n\nc")
                )
            )
        );
    }

    public void testFilteringOfWhitespaceOnlyLines() throws InvariantViolation {
        assertEquals(
            List.of("a", "b"),
            this.content(
                new SignificantLines(
                    new Text.Of("a\n   \n\t\n  \nb")
                )
            )
        );
    }

    public void testFilteringOfLeadingAndTrailingNewlines()
        throws InvariantViolation {
        assertEquals(
            List.of("a", "b"),
            this.content(
                new SignificantLines(
                    new Text.Of("\n\n\na\nb\n\n\n")
                )
            )
        );
    }

    public void testOnlyEmptyLines() throws InvariantViolation {
        assertEquals(
            List.of(),
            new SignificantLines(
                new Text.Of("\n\n\n   \n\t\n")
            ).contents()
        );
    }

    public void testOnlyWhitespace() throws InvariantViolation {
        assertEquals(
            List.of(),
            new SignificantLines(
                new Text.Of("    \t   ")
            ).contents()
        );
    }

    public void testSingleNewline() throws InvariantViolation {
        assertEquals(
            List.of(),
            new SignificantLines(new Text.Of("\n")).contents()
        );
    }

    public void testOrderPreservative() throws InvariantViolation {
        assertEquals(
            List.of("first", "second", "third"),
            this.content(
                new SignificantLines(
                    new Text.Of("first\nsecond\nthird")
                )
            )
        );
    }

    public void testInnerSpacesPreservative() throws InvariantViolation {
        assertEquals(
            List.of("hello world", "foo bar"),
            this.content(
                new SignificantLines(
                    new Text.Of("  hello world  \n  foo bar  ")
                )
            )
        );
    }

    public void testBrokenSource() {
        try {
            new SignificantLines(
                () -> {
                    throw new InvariantViolation("There is no text.");
                }
            ).contents();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    private List<String> content(final SignificantLines lines)
        throws InvariantViolation {
        return lines.contents()
            .stream()
            .map(t -> {
                try {
                    return t.content();
                } catch (final InvariantViolation e) {
                    throw new AssertionError(e);
                }
            })
            .toList();
    }
}
