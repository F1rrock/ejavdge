package org.ejavdge.dom.path;

import org.ejavdge.error.InvariantViolation;

@FunctionalInterface
public interface DocPath {
    String view() throws InvariantViolation;

    final class Of implements DocPath {
        private final String src;

        public Of(final String s) {
            this.src = s;
        }

        @Override
        public String view() throws InvariantViolation {
            return this.src;
        }
    }
}
