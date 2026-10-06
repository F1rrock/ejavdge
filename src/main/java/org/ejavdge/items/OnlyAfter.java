package org.ejavdge.items;

import org.ejavdge.error.InvariantViolation;

import java.util.List;
import java.util.function.Predicate;

/**
 * An {@link Items} view that returns only the elements that appear after the
 * first element matching a given predicate.
 * <p>
 * This class wraps a source collection and a predicate. When {@link #contents()}
 * is called, the source collection is materialized, and all elements up to and
 * including the first element that satisfies the predicate are discarded. The
 * remaining elements are returned in their original order.
 * <p>
 * This is useful for extracting the portion of a sequence that follows a known
 * marker or section header. For example, given a list of lines, one might use
 * {@code OnlyAfter} to get all lines after the line equal to {@code "Examples"}.
 *
 * @param <T> the type of elements in the collection
 */
public final class OnlyAfter<T> implements Items<T> {

    /**
     * The source collection whose elements are filtered.
     */
    private final Items<T> origin;

    /**
     * The predicate that determines the cut-off point.
     */
    private final Predicate<T> predicate;

    /**
     * Creates a filtered collection that returns only the elements after the
     * first element matching the given predicate.
     *
     * @param f  the predicate that identifies the cut-off element
     * @param xs the source collection
     */
    public OnlyAfter(final Predicate<T> f, final Items<T> xs) {
        this.predicate = f;
        this.origin = xs;
    }

    /**
     * Returns the elements that follow the first element matching the
     * predicate.
     * <p>
     * The source collection is materialized, elements are skipped until (and
     * including) the first one that satisfies the predicate, and the remainder
     * is returned as a list. If no element satisfies the predicate, the result
     * is an empty list.
     *
     * @return the list of elements after the first matching element
     * @throws InvariantViolation if an invariant is violated while
     *         materializing the source collection
     */
    @Override
    public List<T> contents() throws InvariantViolation {
        return this.origin.contents()
            .stream()
            .dropWhile(this.predicate.negate())
            .skip(1)
            .toList();
    }
}
