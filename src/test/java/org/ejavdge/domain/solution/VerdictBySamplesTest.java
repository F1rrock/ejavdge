package org.ejavdge.domain.solution;

import junit.framework.TestCase;
import org.ejavdge.domain.Fixture;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.items.Items;
import org.ejavdge.scalar.text.Text;

import java.util.concurrent.atomic.AtomicInteger;

public final class VerdictBySamplesTest extends TestCase {
    private final Fixture sum = new Fixture.Of(
        new Text.Of("1\n2\n"),
        new Text.Of("3\n")
    );
    private final Fixture echo = new Fixture.Of(
        new Text.Of("hello"),
        new Text.Of("hello")
    );

    public void testAllFixturesPass() {
        assertTrue(
            new VerdictBySamples(
                Text::content,
                new Items.Of<>(this.echo)
            ).ok()
        );
    }

    public void testOneFixtureFails() {
        assertFalse(
            new VerdictBySamples(
                Text::content,
                new Items.Of<>(this.echo, this.sum)
            ).ok()
        );
    }

    public void testWithoutFixtures() {
        assertTrue(
            new VerdictBySamples(
                Text::content,
                new Items.Of<>()
            ).ok()
        );
    }

    public void testWithTrailingWhitespaces() {
        assertTrue(
            new VerdictBySamples(
                input -> input.content() + "\n",
                new Items.Of<>(
                    new Fixture.Of(
                        new Text.Of("1"),
                        new Text.Of("1")
                    )
                )
            ).ok()
        );
    }

    public void testWithLeadingWhitespace() {
        assertTrue(
            new VerdictBySamples(
                input -> "  " + input.content(),
                new Items.Of<>(
                    new Fixture.Of(
                        new Text.Of("1"),
                        new Text.Of("1")
                    )
                )
            ).ok()
        );
    }

    public void testBothSidesTrimmed() {
        assertTrue(
            new VerdictBySamples(
                input -> "\n" + input.content() + "\n",
                new Items.Of<>(
                    new Fixture.Of(
                        new Text.Of("  1  "),
                        new Text.Of("  1  ")
                    )
                )
            ).ok()
        );
    }

    public void testInputPassedToProgram() {
        assertTrue(
            new VerdictBySamples(
                input -> input.content().toUpperCase(),
                new Items.Of<>(
                    new Fixture.Of(
                        new Text.Of("abc"),
                        new Text.Of("ABC")
                    )
                )
            ).ok()
        );
    }

    public void testShortCircuitOnFirstFailure() {
        final var calls = new AtomicInteger(0);
        new VerdictBySamples(
            input -> {
                calls.incrementAndGet();
                return "wrong";
            },
            new Items.Of<>(
                new Fixture.Of(
                    new Text.Of("1"),
                    new Text.Of("1")
                ),
                new Fixture.Of(
                    new Text.Of("2"),
                    new Text.Of("2")
                ),
                new Fixture.Of(
                    new Text.Of("3"),
                    new Text.Of("3")
                )
            )
        ).ok();
        assertEquals(1, calls.get());
    }

    public void testBrokenProgram() {
        try {
            new VerdictBySamples(
                input -> {
                    throw new InvariantViolation("There is no outcome.");
                },
                new Items.Of<>(this.echo)
            ).ok();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testBrokenFixtures() {
        try {
            new VerdictBySamples(
                Text::content,
                () -> {
                    throw new InvariantViolation("There are no fixtures.");
                }
            ).ok();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }
}
