package org.ejavdge.domain.solution;

import org.ejavdge.domain.Fixture;
import org.ejavdge.domain.Verdict;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.file.Program;
import org.ejavdge.items.Items;
import org.ejavdge.scalar.text.Text;

/**
 * A verdict that determines whether a program passes a set of example tests
 * (fixtures).
 * <p>
 * This class implements {@link Verdict} and checks the outcome of running a
 * given {@link Program} against each {@link Fixture} in a collection. For every
 * fixture, the program's actual output is compared to the expected output after
 * trimming leading and trailing whitespace. The verdict is successful only if
 * the program's trimmed output matches the trimmed expected output for
 * <em>all</em> fixtures.
 * <p>
 * This is typically used for probing a solution against the sample tests
 * provided in a problem statement before submitting it to the contest system.
 */
public final class VerdictBySamples implements Verdict {

    /**
     * The program to be tested against the fixtures.
     */
    private final Program program;

    /**
     * The collection of example fixtures (input/output pairs) to test against.
     */
    private final Items<Fixture> fixtures;

    /**
     * Creates a verdict that tests the given program against the provided
     * fixtures.
     *
     * @param p  the program to run and compare outputs
     * @param fs the collection of example fixtures to test against
     */
    public VerdictBySamples(final Program p, final Items<Fixture> fs) {
        this.program = p;
        this.fixtures = fs;
    }

    /**
     * Returns whether the program passes all example fixtures.
     * <p>
     * For each fixture, the program is executed with the fixture's input, and
     * its output is compared to the fixture's expected output after trimming
     * whitespace from both. The method returns {@code true} only if every
     * fixture passes this comparison; otherwise, it returns {@code false}.
     *
     * @return {@code true} if the program passes all fixtures, {@code false}
     *         otherwise
     * @throws InvariantViolation if an invariant is violated while running the
     *         program or accessing the fixtures
     */
    @Override
    public boolean ok() throws InvariantViolation {
        return this.fixtures.contents()
            .stream()
            .allMatch(
                f -> this.program.outcomeOf(
                    new Text.Of(f.input())
                ).trim().equals(f.expected().trim())
            );
    }
}
