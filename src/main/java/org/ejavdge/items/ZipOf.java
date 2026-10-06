package org.ejavdge.items;

import org.ejavdge.error.InvariantViolation;

import java.util.List;

/**
 * An {@link Items} view that transposes a collection of collections, producing
 * groups whose {@code i}-th element is a collection of the {@code i}-th elements
 * of the original collections.
 * <p>
 * This is the {@code Items} counterpart of a zip operation over multiple
 * sequences. Given several collections of the same element type, {@code ZipOf}
 * produces a collection of collections, where each inner collection groups
 * together the elements that share the same position across the input
 * collections.
 * <p>
 * The transposition is performed recursively:
 * <ol>
 *   <li>If the input collection is empty, the result is an empty list.</li>
 *   <li>Otherwise, the first element of each input collection is taken (via
 *       {@link OnlyFirst}) and grouped into a new collection.</li>
 *   <li>The remaining elements of each input collection (via
 *       {@link WithoutFirst}) are zipped in the same way, and the resulting
 *       groups are appended after the first.</li>
 * </ol>
 * <p>
 * The number of resulting groups equals the length of the shortest input
 * collection: elements beyond that length in longer collections are not
 * included.
 * <p>
 * This is useful for pairing related sequences element-wise, such as combining
 * column labels with their corresponding values in a table row, producing
 * entries like {@code (label, value)}.
 *
 * @param <T> the type of elements in the inner collections
 */
public final class ZipOf<T> implements Items<Items<T>> {

    /**
     * The collection of collections to be transposed.
     */
    private final Items<Items<T>> src;

    /**
     * Creates a transposed collection from the given collections.
     *
     * @param xss the collections to transpose
     */
    @SafeVarargs
    public ZipOf(final Items<T> ...xss) {
        this(new Items.Of<>(xss));
    }

    /**
     * Creates a transposed collection from the given collection of collections.
     *
     * @param xss the collection of collections to transpose
     */
    public ZipOf(final Items<Items<T>> xss) {
        this.src = xss;
    }

    /**
     * Returns the transposed collection of collections.
     * <p>
     * Each resulting inner collection contains the elements at the same
     * position across the input collections. If the input collection is empty,
     * an empty list is returned. The number of resulting groups is limited by
     * the shortest input collection; surplus elements in longer collections are
     * ignored.
     *
     * @return the list of grouped collections obtained by transposing the input
     * @throws InvariantViolation if an invariant is violated while
     *         materializing the input collections or building the groups
     */
    @Override
    public List<Items<T>> contents() throws InvariantViolation {
        final var head = new Joint<>(
            new Map<>(
                OnlyFirst::new,
                this.src
            )
        ).contents();
        if (head.isEmpty()) {
            return List.of();
        }
        return new Joint<>(
            new Items.Of<>(
                new Items.Of<>(head)
            ),
            new ZipOf<>(
                new Map<>(
                    WithoutFirst::new,
                    this.src
                )
            )
        ).contents();
    }
}
