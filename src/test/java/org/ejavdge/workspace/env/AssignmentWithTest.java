package org.ejavdge.workspace.env;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

import java.util.concurrent.atomic.AtomicInteger;

public final class AssignmentWithTest extends TestCase {
    public void testCalling() {
        final var calls = new AtomicInteger(0);
        new AssignmentWith(
            new Text.Of("value"),
            new EnvVariable() {
                @Override
                public String value() throws InvariantViolation {
                    throw new AssertionError("value was called");
                }

                @Override
                public void assignWith(final Text v) throws InvariantViolation {
                    calls.incrementAndGet();
                }
            }
        ).perform();
        assertEquals(1, calls.get());
    }

    public void testValuePropagation() {
        new AssignmentWith(
            new Text.Of("value"),
            new EnvVariable() {
                @Override
                public String value() throws InvariantViolation {
                    throw new AssertionError("value was called");
                }

                @Override
                public void assignWith(final Text v) throws InvariantViolation {
                    assertEquals("value", v.content());
                }
            }
        ).perform();
    }

    public void testBrokenValue() {
        try {
            new AssignmentWith(
                () -> {
                    throw new InvariantViolation("There is no value");
                },
                new EnvVariable() {
                    @Override
                    public String value() throws InvariantViolation {
                        throw new AssertionError("value was called");
                    }

                    @Override
                    public void assignWith(final Text v) throws InvariantViolation {
                        v.content();
                    }
                }
            ).perform();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testBrokenVariable() {
        try {
            new AssignmentWith(
                new Text.Of("value"),
                new EnvVariable() {
                    @Override
                    public String value() throws InvariantViolation {
                        throw new AssertionError("value was called");
                    }

                    @Override
                    public void assignWith(final Text v) throws InvariantViolation {
                        throw new InvariantViolation("There is no variable");
                    }
                }
            ).perform();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }
}
