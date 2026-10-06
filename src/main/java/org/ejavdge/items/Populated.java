package org.ejavdge.items;

import org.ejavdge.error.InvariantViolation;

import java.util.List;

/**
 * An {@link Items} decorator that requires the underlying collection to be
 * non-empty.
 * <p>
 * This class wraps another {@link Items} instance and enforces an invariant:
 * when {@link #contents()} is called, the underlying collection is materialized
 * and, if it turns out to be empty, an {@link InvariantViolation} is thrown
 * with the message {@code "Items is not populated"}. If the collection contains
 * at least one element, it is returned unchanged.
 * <p>
 * This is useful when an empty collection would indicate a violation of the
 * application's assumptions — for example, when a set of required elements
 * (such as form fields or configuration entries) must be present for a
 * subsequent operation to make sense.
 *
 * @param <T> the type of elements in the collection
 */
public final class Populated<T> implements Items<T> {

    /**
     * The underlying collection that must be non-empty.
     */
    private final Items<T> origin;

    /**
     * Creates a decorator that requires the given collection to be non-empty.
     *
     * @param xs the collection that must be populated
     */
    public Populated(final Items<T> xs) {
        this.origin = xs;
    }

    /**
     * Returns the contents of the underlying collection, requiring it to be
     * non-empty.
     * <p>
     * The underlying collection is materialized. If it is empty, an
     * {@link InvariantViolation} is thrown with the message
     * {@code "Items is not populated"}. Otherwise, the list of elements is
     * returned unchanged.
     *
     * @return the non-empty list of elements
     * @throws InvariantViolation if the underlying collection is empty or if an
     *         invariant is violated while materializing its contents
     */
    @Override
    public List<T> contents() throws InvariantViolation {
        final var list = this.origin.contents();
        if (list.isEmpty()) {
            throw new InvariantViolation("Items is not populated");
        }
        return list;
    }
}
