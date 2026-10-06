package org.ejavdge.items;

import org.ejavdge.error.InvariantViolation;

import java.util.List;
import java.util.function.Predicate;

/**
 * An {@link Items} view that returns only the elements satisfying a given
 * predicate.
 * <p>
 * This class wraps a source collection and a predicate. When
 * {@link #contents()} is called, the source collection is materialized, and
 * only those elements for which the predicate returns {@code true} are
 * retained. The order of the retained elements is preserved.
 * <p>
 * This is the {@code Items} counterpart of
 * {@link java.util.stream.Stream#filter}, and it is the general-purpose
 * filtering combinator used throughout the application. For position-based
 * filtering (before, after, or a fixed number of leading elements), consider
 * {@link OnlyUntil}, {@link OnlyAfter}, or {@link OnlyFirst} respectively.
 *
 * @param <T> the type of elements in the collection
 */
public final class OnlyWhere<T> implements Items<T> {

    /**
     * The predicate used to select elements from the source collection.
     */
    private final Predicate<T> predicate;

    /**
     * The source collection whose elements are filtered.
     */
    private final Items<T> origin;

    /**
     * Creates a filtered collection from the given predicate and source
     * collection.
     *
     * @param f  the predicate that determines which elements are retained
     * @param xs the source collection
     */
    public OnlyWhere(final Predicate<T> f, final Items<T> xs) {
        this.predicate = f;
        this.origin = xs;
    }

    /**
     * Returns the elements of the source collection that satisfy the
     * predicate.
     * <p>
     * The source collection is materialized, and each element is tested against
     * the predicate. Only elements for which the predicate returns
     * {@code true} are included in the result, in their original order.
     *
     * @return the list of elements satisfying the predicate
     * @throws InvariantViolation if an invariant is violated while
     *         materializing the source collection
     */
    @Override
    public List<T> contents() throws InvariantViolation {
        return this.origin.contents()
            .stream()
            .filter(this.predicate)
            .toList();
    }
}
