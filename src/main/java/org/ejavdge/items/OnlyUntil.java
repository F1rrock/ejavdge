package org.ejavdge.items;

import org.ejavdge.error.InvariantViolation;

import java.util.List;
import java.util.function.Predicate;

/**
 * An {@link Items} view that returns only the elements that appear before the
 * first element matching a given predicate.
 * <p>
 * This class wraps a source collection and a predicate. When {@link #contents()}
 * is called, the source collection is materialized, and elements are taken from
 * the beginning of the sequence until (but not including) the first element
 * that satisfies the predicate. All preceding elements are returned in their
 * original order.
 * <p>
 * This is useful for extracting the portion of a sequence that precedes a
 * known marker or section header. For example, given a list of lines, one might
 * use {@code OnlyUntil} to get all lines before the line equal to
 * {@code "Output"}.
 *
 * @param <T> the type of elements in the collection
 */
public final class OnlyUntil<T> implements Items<T> {

    /**
     * The source collection whose elements are filtered.
     */
    private final Items<T> origin;

    /**
     * The predicate that determines where the sequence is cut off.
     */
    private final Predicate<T> predicate;

    /**
     * Creates a filtered collection that returns only the elements before the
     * first element matching the given predicate.
     *
     * @param f  the predicate that identifies the cut-off element
     * @param xs the source collection
     */
    public OnlyUntil(final Predicate<T> f, final Items<T> xs) {
        this.predicate = f;
        this.origin = xs;
    }

    /**
     * Returns the elements that precede the first element matching the
     * predicate.
     * <p>
     * The source collection is materialized, and elements are taken from the
     * beginning until the first element that satisfies the predicate is
     * encountered (that element itself is not included). If no element
     * satisfies the predicate, the entire source collection is returned.
     *
     * @return the list of elements before the first matching element
     * @throws InvariantViolation if an invariant is violated while
     *         materializing the source collection
     */
    @Override
    public List<T> contents() throws InvariantViolation {
        return this.origin.contents()
            .stream()
            .takeWhile(this.predicate.negate())
            .toList();
    }
}
