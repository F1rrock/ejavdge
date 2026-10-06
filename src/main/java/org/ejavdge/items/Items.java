package org.ejavdge.items;

import org.ejavdge.error.InvariantViolation;

import java.util.List;

/**
 * A lazy sequence of items that is materialized on demand.
 * <p>
 * This is the central abstraction for working with collections throughout the
 * application. Rather than holding a concrete list of elements, an
 * {@code Items} instance represents a computation that, when asked via
 * {@link #contents()}, produces a {@link List} of elements. This allows
 * collections to be composed, transformed, and filtered lazily, deferring
 * expensive work until the result is actually needed.
 * <p>
 * This is a functional interface whose functional method is
 * {@link #contents()}. A number of combinators (such as
 * {@link org.ejavdge.items.Map}, {@link org.ejavdge.items.BindOfItems},
 * {@link org.ejavdge.items.Lines}, and others in this package) build on this
 * interface to express complex transformations declaratively.
 *
 * @param <T> the type of elements in the collection
 */
@FunctionalInterface
public interface Items<T> {

    /**
     * Returns the underlying content as a list of elements.
     * <p>
     * Calling this method materializes the collection. Implementations may
     * perform expensive operations (such as network requests or parsing) as
     * part of this call.
     *
     * @return the list of elements
     * @throws InvariantViolation if an invariant is violated while materializing
     *         the contents
     */
    List<T> contents() throws InvariantViolation;

    /**
     * A simple {@link Items} implementation that wraps a fixed list of
     * elements.
     * <p>
     * The elements can be provided either as varargs or as an existing
     * {@link List}. The internal list is defensively copied on construction and
     * on each call to {@link #contents()}, so callers cannot mutate the
     * underlying state.
     *
     * @param <T> the type of elements in the collection
     */
    final class Of<T> implements Items<T> {

        /**
         * The wrapped list of elements.
         */
        private final List<T> xs;

        /**
         * Creates a collection from the given elements.
         *
         * @param xs the elements of the collection
         */
        @SafeVarargs
        public Of(final T ...xs) {
            this(List.of(xs));
        }

        /**
         * Creates a collection from the given list of elements.
         *
         * @param xs the list of elements to wrap
         */
        public Of(final List<T> xs) {
            this.xs = List.copyOf(xs);
        }

        /**
         * Returns the wrapped list of elements as an immutable copy.
         *
         * @return the list of elements
         */
        @Override
        public List<T> contents() {
            return List.copyOf(this.xs);
        }
    }
}
