package org.ejavdge.scalar.text;

import org.ejavdge.error.InvariantViolation;

@FunctionalInterface
public interface Text {
    String content() throws InvariantViolation;

    final class Of implements Text {
        private final String x;

        public Of(final String x) {
            this.x = x;
        }

        @Override
        public String content() {
            return this.x;
        }
    }
}
