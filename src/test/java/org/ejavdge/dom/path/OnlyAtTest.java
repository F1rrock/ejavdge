package org.ejavdge.dom.path;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.num.Num;

public final class OnlyAtTest extends TestCase {
    public void testAtFirstIndex() {
        assertEquals(
            "//*[1]",
            new OnlyAt(
                new Num.Of(1),
                new DocPath.Of("//*")
            ).view()
        );
    }

    public void testAtZeroIndex() {
        try {
            new OnlyAt(
                new Num.Of(0),
                new DocPath.Of("//*")
            ).view();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testAtNegativeIndex() {
        try {
            new OnlyAt(
                new Num.Of(-1),
                new DocPath.Of("//*")
            ).view();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testWithNonRoot() {
        assertEquals(
            "//*[@class = 't1'][1]",
            new OnlyAt(
                new Num.Of(1),
                new DocPath.Of("//*[@class = 't1']")
            ).view()
        );
    }

    public void testBrokenPath() {
        try {
            new OnlyAt(
                new Num.Of(1),
                () -> {
                    throw new InvariantViolation("There is no xpath.");
                }
            ).view();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testBrokenIndex() {
        try {
            new OnlyAt(
                () -> {
                    throw new InvariantViolation("There is no index");
                },
                new DocPath.Of("//*")
            ).view();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }
}
