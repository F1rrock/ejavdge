package org.ejavdge.scalar.text;

import junit.framework.TestCase;
import org.ejavdge.scalar.num.Num;

public final class TextOfNumTest extends TestCase {
    public void testTen() {
        assertEquals(
            "10",
            new TextOfNum(
                new Num.Of(10)
            ).content()
        );
    }

    public void testMinusTen() {
        assertEquals(
            "-10",
            new TextOfNum(
                new Num.Of(-10)
            ).content()
        );
    }
}
