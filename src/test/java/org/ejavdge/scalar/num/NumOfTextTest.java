package org.ejavdge.scalar.num;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

public final class NumOfTextTest extends TestCase {
    public void testTen() {
        assertEquals(
            10,
            new NumOfText(
                new Text.Of("10")
            ).value()
        );
    }

    public void testMinusTen() {
        assertEquals(
                -10,
                new NumOfText(
                        new Text.Of("-10")
                ).value()
        );
    }

    public void testNonNum() {
        try {
            new NumOfText(new Text.Of("Non num")).value();
        } catch (final InvariantViolation v) {
            return;
        }
        fail("InvariantViolation expected");
    }
}
