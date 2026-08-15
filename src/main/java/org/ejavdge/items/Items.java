package org.ejavdge.items;

import org.ejavdge.error.InvariantViolation;

import java.util.List;

@FunctionalInterface
public interface Items<T> {
    List<T> contents() throws InvariantViolation;

    final class Of<T> implements Items<T> {
        private final List<T> xs;

        @SafeVarargs
        public Of(final T ...xs) {
            this(List.of(xs));
        }

        public Of(final List<T> xs) {
            this.xs = List.copyOf(xs);
        }

        @Override
        public List<T> contents() {
            return List.copyOf(this.xs);
        }
    }
}
