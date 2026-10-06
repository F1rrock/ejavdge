package org.ejavdge.items;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.num.NonNegative;
import org.ejavdge.scalar.num.Num;

import java.util.List;

/**
 * An {@link Items} view that skips a given number of leading elements from a
 * source collection.
 * <p>
 * This class wraps a source collection and a count, and exposes a collection
 * that omits the first {@code n} elements. The count is represented by a
 * {@link Num} and is wrapped in a {@link NonNegative} to ensure it is not
 * negative. By default, the count is {@code 1}, so the resulting collection
 * drops only the very first element of the source.
 * <p>
 * This is the counterpart of {@link OnlyFirst}: while {@code OnlyFirst} keeps
 * the leading elements, {@code WithoutFirst} discards them. It is useful when
 * the first element of a sequence is known to be a header, marker, or otherwise
 * uninteresting, and only the remainder is needed.
 *
 * @param <T> the type of elements in the collection
 */
public final class WithoutFirst<T> implements Items<T> {

    /**
     * The number of leading elements to skip.
     */
    private final Num count;

    /**
     * The source collection whose leading elements are skipped.
     */
    private final Items<T> origin;

    /**
     * Creates a collection that skips the first element of the source
     * collection.
     *
     * @param xs the source collection
     */
    public WithoutFirst(final Items<T> xs) {
        this(new Num.Of(1), xs);
    }

    /**
     * Creates a collection that skips the first {@code n} elements of the
     * source collection.
     * <p>
     * The provided count is wrapped in a {@link NonNegative} to ensure it is
     * not negative.
     *
     * @param n  the number of leading elements to skip
     * @param xs the source collection
     */
    public WithoutFirst(final Num n, final Items<T> xs) {
        this.count = new NonNegative(n);
        this.origin = xs;
    }

    /**
     * Returns the elements of the source collection after skipping the first
     * {@code n} elements.
     * <p>
     * The source collection is materialized, and a stream skip is applied using
     * the configured count. If the source contains fewer than {@code n}
     * elements, the result is an empty list. The order of the remaining
     * elements is preserved.
     *
     * @return the list of elements after the skipped leading elements
     * @throws InvariantViolation if an invariant is violated while
     *         materializing the source collection or retrieving the count
     */
    @Override
    public List<T> contents() throws InvariantViolation {
        return this.origin.contents()
            .stream()
            .skip(this.count.value())
            .toList();
    }
}
