package org.ejavdge.file;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

/**
 * A contract for a program that can be executed with a given input and
 * produces a textual outcome.
 * <p>
 * Implementations of this interface represent executable programs, such as
 * solutions to contest problems. The single method, {@link #outcomeOf(Text)},
 * takes the program's input as a {@link Text} and returns the program's output
 * as a {@link String}.
 * <p>
 * This is a functional interface whose functional method is
 * {@link #outcomeOf(Text)}. It is used by sample-testing logic (for example,
 * {@link org.ejavdge.domain.solution.VerdictBySamples}) to run a solution
 * against example inputs and compare the actual output with the expected
 * output.
 */
@FunctionalInterface
public interface Program {

    /**
     * Runs this program with the given input and returns its output.
     *
     * @param i the input data to feed to the program
     * @return the program's output as a string
     * @throws InvariantViolation if an invariant is violated during execution,
     *         indicating that the program could not be run or did not complete
     *         successfully
     */
    String outcomeOf(final Text i) throws InvariantViolation;
}
