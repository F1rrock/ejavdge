package org.ejavdge.app;

import junit.framework.TestCase;
import org.ejavdge.app.scenario.SubmittingWithConfirmation;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

import java.util.concurrent.atomic.AtomicInteger;

public final class SubmitWithNotificationAppTest extends TestCase {
    public void testNotice() {
        final var buffer = new StringBuilder();
        new SubmitWithNotificationApp(
            new SubmittingWithConfirmation(() -> {}),
            text -> buffer.append(text.content())
        ).run();
        assertFalse(buffer.toString().isEmpty());
    }

    public void testTotalSubmits() {
        final var calls = new AtomicInteger(0);
        new SubmitWithNotificationApp(
            new SubmittingWithConfirmation(calls::incrementAndGet),
            Text::content
        ).run();
        assertEquals(1, calls.get());
    }

    public void testBrokenSubmission() {
        try {
            new SubmitWithNotificationApp(
                new SubmittingWithConfirmation(() -> {
                    throw new InvariantViolation("There is invalid submission.");
                }),
                Text::content
            ).run();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }
}
