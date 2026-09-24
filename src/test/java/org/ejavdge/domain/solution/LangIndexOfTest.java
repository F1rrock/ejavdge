package org.ejavdge.domain.solution;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.file.ByteFile;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.text.Text;

import java.nio.charset.StandardCharsets;

public final class LangIndexOfTest extends TestCase {
    public void testLanguageMarkerAtStart() {
        assertEquals(
            14,
            new LangIndexOf(this.file("// language: 14\nint main() {}"))
                .value()
        );
    }

    public void testLanguageMarkerInTheMiddle() {
        assertEquals(
            82,
            new LangIndexOf(this.file(
                """
                // some comment
                int x;
                // language: 82
                int y;
                """
            )).value()
        );
    }

    public void testLanguageMarkerWithTrailingText() {
        assertEquals(
            42,
            new LangIndexOf(this.file(
                "// language: 42\nrest of file"
            )).value()
        );
    }

    public void testSeveralLanguageMarkers() {
        assertEquals(
            1,
            new LangIndexOf(this.file(
                """
                // language: 1
                // language: 2
                // language: 3
                """
            )).value()
        );
    }

    public void testMarkerWithoutSpace() {
        assertEquals(
            14,
            new LangIndexOf(this.file(
                "// language:14\nint x;"
            )).value()
        );
    }

    public void testMarkerWithLeadingSpaces() {
        try {
            new LangIndexOf(this.file(
                "  // language: 14\nint x;"
            )).value();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testMarkerNotAtLineStart() {
        try {
            new LangIndexOf(this.file(
                "int x; // language: 14"
            )).value();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testInvalidMarker() {
        try {
            new LangIndexOf(this.file(
                "// lang: 14\nint x;"
            )).value();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testWithoutMarker() {
        try {
            new LangIndexOf(this.file("int main() {}")).value();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testEmptyFile() {
        try {
            new LangIndexOf(this.file("")).value();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testZeroLanguage() {
        try {
            new LangIndexOf(this.file("// language: 0\nint x;")).value();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testNegativeLanguage() {
        try {
            new LangIndexOf(this.file("// language: -1\nint x;")).value();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testBrokenFile() {
        try {
            new LangIndexOf(
                () -> {
                    throw new InvariantViolation("There is no file.");
                }
            ).value();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    private ByteFile file(final String content) {
        return new ByteFile.Of(
            new Text.Of("Main.java"),
            new Bytes.Of(content.getBytes(StandardCharsets.UTF_8))
        );
    }
}
