package org.ejavdge.scalar.num;

import org.ejavdge.error.InvariantViolation;

public interface Num {
    int value() throws InvariantViolation;

    final class Of implements Num {
        private final int src;

        public Of(final int n) {
            this.src = n;
        }

        @Override
        public int value() throws InvariantViolation {
            return this.src;
        }
    }
}
