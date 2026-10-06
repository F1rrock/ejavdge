package org.ejavdge.items;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.num.NonNegative;
import org.ejavdge.scalar.num.Num;

import java.util.List;

/**
 * An {@link Items} view that returns only the first {@code n} elements of a
 * source collection.
 * <p>
 * This class wraps a source collection and a count, and exposes a collection
 * that is limited to at most that many elements. The count is represented by a
 * {@link Num} and is wrapped in a {@link NonNegative} to ensure it is not
 * negative. By default, the count is {@code 1}, so the resulting collection
 * contains at most a single element.
 * <p>
 * This is useful when only a fixed number of leading elements from a sequence
 * is needed, for example to select the first line or the first few lines of a
 * report.
 *
 * @param <T> the type of elements in the collection
 */
public final class OnlyFirst<T> implements Items<T> {

    /**
     * The maximum number of elements to return.
     */
    private final Num count;

    /**
     * The source collection whose elements are limited.
     */
    private final Items<T> origin;

    /**
     * Creates a collection that returns at most the first element of the
     * source collection.
     *
     * @param xs the source collection
     */
    public OnlyFirst(final Items<T> xs) {
        this(new Num.Of(1), xs);
    }

    /**
     * Creates a collection that returns at most the first {@code n} elements
     * of the source collection.
     * <p>
     * The provided count is wrapped in a {@link NonNegative} to ensure it is
     * not negative.
     *
     * @param n  the maximum number of elements to return
     * @param xs the source collection
     */
    public OnlyFirst(final Num n, final Items<T> xs) {
        this.count = new NonNegative(n);
        this.origin = xs;
    }

    /**
     * Returns the first {@code n} elements of the source collection, or fewer
     * if the source contains fewer elements.
     * <p>
     * The source collection is materialized, and a stream limit is applied
     * using the configured count. The order of the elements is preserved.
     *
     * @return the list of at most {@code n} leading elements from the source
     *         collection
     * @throws InvariantViolation if an invariant is violated while
     *         materializing the source collection or retrieving the count
     */
    @Override
    public List<T> contents() throws InvariantViolation {
        return this.origin.contents()
            .stream()
            .limit(this.count.value())
            .toList();
    }
}
