package org.ejavdge.workspace.env;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

public final class ValueOfTest extends TestCase {
    public void testValidVariable() {
        assertEquals(
            "hello",
            new ValueOf(
                new EnvVariable() {
                    @Override
                    public String value() throws InvariantViolation {
                        return "hello";
                    }

                    @Override
                    public void assignWith(final Text v) throws InvariantViolation {
                        throw new AssertionError("assignWith was called");
                    }
                }
            ).content()
        );
    }

    public void testBrokenVariable() {
        try {
            new ValueOf(
                new EnvVariable() {
                    @Override
                    public String value() throws InvariantViolation {
                        throw new InvariantViolation("there is no value");
                    }

                    @Override
                    public void assignWith(final Text v) throws InvariantViolation {
                        throw new AssertionError("assignWith was called");
                    }
                }
            ).content();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }
}
