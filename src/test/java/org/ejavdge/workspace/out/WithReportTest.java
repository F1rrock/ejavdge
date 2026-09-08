package org.ejavdge.workspace.out;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

public final class WithReportTest extends TestCase {
    public void testWritingOfText() throws InvariantViolation {
        new WithReport(
            t -> assertEquals(
                "hello",
                t.content()
            )
        ).write(new Text.Of("hello"));
    }

    public void testReportingError() throws InvariantViolation {
        final var output = new StringBuilder();
        new WithReport(t -> output.append(t.content())).write(
            () -> {
                throw new InvariantViolation(
                    "There is no text",
                    new InvariantViolation(
                        "There is no origin"
                    )
                );
            }
        );
        assertEquals(
            "\u001B[31mError: There is no text" +
                "\nCaused by: There is no origin" +
                "\u001B[0m",
            output.toString()
        );
    }
}
