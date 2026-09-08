package org.ejavdge.workspace.out;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

import java.util.concurrent.atomic.AtomicInteger;

public final class WritingOfTest extends TestCase {
    public void testWriting() throws InvariantViolation {
        final var calls = new AtomicInteger(0);
        new WritingOf(
            new Text.Of("hello"),
            t -> {
                calls.incrementAndGet();
                assertEquals("hello", t.content());
            }
        ).perform();
        if (calls.get() <= 0) {
            fail("logger has not been called");
        }
    }

    public void testBrokenOut() {
        try {
            new WritingOf(
                new Text.Of("hello"),
                t -> {
                    throw new InvariantViolation("There is no output");
                }
            ).perform();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }
}
