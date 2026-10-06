package org.ejavdge.domain;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

/**
 * A single test case for a problem, consisting of input data and the expected
 * output data.
 * <p>
 * A fixture is used to verify the correctness of a solution by running the
 * program with the given input and comparing its actual output with the
 * expected output. Implementations of this interface provide access to both
 * parts as strings.
 */
public interface Fixture {

    /**
     * Returns the input data for this fixture.
     *
     * @return the input data as a string
     * @throws InvariantViolation if an invariant is violated while retrieving
     *         the input
     */
    String input() throws InvariantViolation;

    /**
     * Returns the expected output data for this fixture.
     *
     * @return the expected output as a string
     * @throws InvariantViolation if an invariant is violated while retrieving
     *         the expected output
     */
    String expected() throws InvariantViolation;

    /**
     * A simple {@link Fixture} implementation that wraps two {@link Text}
     * objects: one for the input data and one for the expected output.
     */
    final class Of implements Fixture {

        /**
         * The input data for this fixture.
         */
        private final Text in;

        /**
         * The expected output data for this fixture.
         */
        private final Text exp;

        /**
         * Creates a new fixture from the given input and expected output.
         *
         * @param i the input data
         * @param e the expected output data
         */
        public Of(final Text i, final Text e) {
            this.in = i;
            this.exp = e;
        }

        /**
         * Returns the input data for this fixture.
         *
         * @return the input data as a string
         * @throws InvariantViolation if an invariant is violated while
         *         retrieving the input
         */
        @Override
        public String input() throws InvariantViolation {
            return this.in.content();
        }

        /**
         * Returns the expected output data for this fixture.
         *
         * @return the expected output as a string
         * @throws InvariantViolation if an invariant is violated while
         *         retrieving the expected output
         */
        @Override
        public String expected() throws InvariantViolation {
            return this.exp.content();
        }
    }
}
