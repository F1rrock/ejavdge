package org.ejavdge.scalar.num;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;

public final class NumOfHexTest extends TestCase {
    public void testHexA() {
        assertEquals(10, new NumOfHex("A").value());
    }

    public void testHexF() {
        assertEquals(15, new NumOfHex("F").value());
    }

    public void testHexLowercase() {
        assertEquals(15, new NumOfHex("f").value());
    }

    public void testHexFF() {
        assertEquals(255, new NumOfHex("FF").value());
    }

    public void testHex1A() {
        assertEquals(26, new NumOfHex("1A").value());
    }

    public void testHexZero() {
        assertEquals(0, new NumOfHex("0").value());
    }

    public void testHexWithPrefix() {
        try {
            new NumOfHex("0x10").value();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testInvalidHex() {
        try {
            new NumOfHex("GHI").value();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testEmptyHex() {
        try {
            new NumOfHex("").value();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testHexWithSpaces() {
        try {
            new NumOfHex(" 10 ").value();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }
}