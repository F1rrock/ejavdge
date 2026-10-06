package org.ejavdge.items;

import org.ejavdge.error.InvariantViolation;

import java.util.List;

/**
 * An {@link Items} implementation that concatenates the contents of several
 * collections into a single flat list.
 * <p>
 * This class represents the concatenation (or "join") of multiple collections
 * of the same element type. When {@link #contents()} is called, each of the
 * nested collections is materialized, and all their elements are flattened into
 * a single list in the order the collections were provided.
 * <p>
 * The nested collections can be passed either as varargs of {@link Items}, or
 * as an {@code Items} of {@code Items}. This makes {@code Joint} useful for
 * combining lists that originate from different sources — for example,
 * appending extra form fields to those already present in a
 * {@link org.ejavdge.contest.ContestForm}.
 *
 * @param <T> the type of elements in the resulting collection
 */
public final class Joint<T> implements Items<T> {

    /**
     * The collection of nested collections to be concatenated.
     */
    private final Items<Items<T>> xss;

    /**
     * Creates a joint collection from the given collections.
     *
     * @param xss the collections to concatenate, in order
     */
    @SafeVarargs
    public Joint(final Items<T> ...xss) {
        this(new Items.Of<>(xss));
    }

    /**
     * Creates a joint collection from the given collection of collections.
     *
     * @param xss the collection of collections to concatenate, in order
     */
    public Joint(final Items<Items<T>> xss) {
        this.xss = xss;
    }

    /**
     * Returns the concatenated contents of all nested collections.
     * <p>
     * Each nested collection is materialized, and its elements are flattened
     * into a single list, preserving the order in which the nested collections
     * were provided. If any nested collection fails to materialize its
     * contents, the corresponding {@link InvariantViolation} is propagated.
     *
     * @return the flattened list of all elements from the nested collections
     * @throws InvariantViolation if any nested collection violates an
     *         invariant while materializing its contents
     */
    @Override
    public List<T> contents() throws InvariantViolation {
        return this.xss.contents()
            .stream()
            .flatMap(xs -> xs.contents().stream())
            .toList();
    }
}
