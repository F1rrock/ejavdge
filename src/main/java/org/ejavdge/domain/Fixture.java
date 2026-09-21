package org.ejavdge.domain;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

public interface Fixture {
    String input() throws InvariantViolation;
    String expected() throws InvariantViolation;

    final class Of implements Fixture {
        private final Text in;
        private final Text exp;

        public Of(final Text i, final Text e) {
            this.in = i;
            this.exp = e;
        }

        @Override
        public String input() throws InvariantViolation {
            return this.in.content();
        }

        @Override
        public String expected() throws InvariantViolation {
            return this.exp.content();
        }
    }
}
