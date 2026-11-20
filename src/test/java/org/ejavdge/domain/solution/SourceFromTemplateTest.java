package org.ejavdge.domain.solution;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.text.Utf8Text;

import java.nio.charset.StandardCharsets;

public final class SourceFromTemplateTest extends TestCase {
    public void testSourceWithHeaderAndFooter() {
        assertEquals(
            "class Main { int answer = 42; }",
            new Utf8Text(
                new SourceFromTemplate(
                    new TemplateOfSolution(
                        new Bytes.Of("class Main { ".getBytes(StandardCharsets.UTF_8)),
                        new Bytes.Of(" }".getBytes(StandardCharsets.UTF_8))
                    ),
                    new Bytes.Of("int answer = 42;".getBytes(StandardCharsets.UTF_8))
                )
            ).content()
        );
    }

    public void testSourceWithoutHeader() {
        assertEquals(
            "class Main {}",
            new Utf8Text(
                new SourceFromTemplate(
                    new TemplateOfSolution(
                        new Bytes.Of(new byte[0]),
                        new Bytes.Of("}".getBytes(StandardCharsets.UTF_8))
                    ),
                    new Bytes.Of("class Main {".getBytes(StandardCharsets.UTF_8))
                )
            ).content()
        );
    }

    public void testBrokenSolutionSource() {
        final var error = new InvariantViolation("There is no solution source.");
        try {
            new SourceFromTemplate(
                new TemplateOfSolution(new Bytes.Of(new byte[0]), new Bytes.Of(new byte[0])),
                () -> {
                    throw error;
                }
            ).content();
        } catch (final InvariantViolation e) {
            assertSame(error, e);
            return;
        }
        fail("InvariantViolation");
    }
}
