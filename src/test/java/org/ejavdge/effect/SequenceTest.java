package org.ejavdge.effect;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;

import java.util.concurrent.atomic.AtomicInteger;

public final class SequenceTest extends TestCase {
    public void testSequenceOfZeroEffects() {
        try {
            new Sequence().perform();
        } catch (final InvariantViolation e) {
            fail(e.getMessage());
        }
    }

    public void testSequenceOfSingleEffect() {
        final var calls = new AtomicInteger(0);
        new Sequence(calls::incrementAndGet).perform();
        assertEquals(1, calls.get());
    }

    public void testSequenceOfSeveralEffects() {
        final var calls = new AtomicInteger(0);
        final Effect effect = calls::incrementAndGet;
        new Sequence(effect, effect, effect).perform();
        assertEquals(3, calls.get());
    }

    public void testOrderOfEffects() {
        final var builder = new StringBuilder();
        new Sequence(
            () -> builder.append('a'),
            () -> builder.append('b'),
            () -> builder.append('c')
        ).perform();
        assertEquals("abc", builder.toString());
    }

    public void testWithBrokenEffect() {
        try {
            new Sequence(
                () -> {
                    throw new InvariantViolation("There is no items.");
                }
            ).perform();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }
}
