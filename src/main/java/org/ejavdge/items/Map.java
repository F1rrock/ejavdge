package org.ejavdge.items;

import org.ejavdge.error.InvariantViolation;

import java.util.List;
import java.util.function.Function;

/**
 * An {@link Items} implementation that lazily transforms each element of a
 * source collection using a mapping function.
 * <p>
 * This class is the {@code Items} counterpart of {@link java.util.stream.Stream#map}.
 * It wraps a source collection of elements of type {@code D} together with a
 * function {@code D -> R}, and exposes a collection of elements of type
 * {@code R}. The transformation is applied lazily: the mapping function is not
 * invoked until {@link #contents()} is called, at which point the source
 * collection is materialized, each element is transformed, and the results are
 * collected into a new list.
 * <p>
 * This is a fundamental building block for composing declarative pipelines over
 * {@code Items} collections, and it is used throughout the application to
 * convert raw extracted values into higher-level domain objects.
 *
 * @param <D> the type of elements in the source collection
 * @param <R> the type of elements in the resulting collection
 */
public final class Map<D, R> implements Items<R> {

    /**
     * The function applied to each element of the source collection.
     */
    private final Function<D, R> mapping;

    /**
     * The source collection whose elements are transformed.
     */
    private final Items<D> prototype;

    /**
     * Creates a mapped collection from the given function and source
     * collection.
     *
     * @param f  the function applied to each element of the source collection
     * @param xs the source collection
     */
    public Map(
        final Function<D, R> f,
        final Items<D> xs
    ) {
        this.mapping = f;
        this.prototype = xs;
    }

    /**
     * Returns the list of transformed elements.
     * <p>
     * The source collection is materialized, each element is passed through the
     * mapping function, and the results are collected into a new list. The
     * order of the elements is preserved. If the source collection fails to
     * materialize its contents, the corresponding {@link InvariantViolation} is
     * propagated.
     *
     * @return the list of transformed elements
     * @throws InvariantViolation if an invariant is violated while
     *         materializing the source collection or applying the mapping
     */
    @Override
    public List<R> contents() throws InvariantViolation {
        return this.prototype.contents()
            .stream()
            .map(this.mapping)
            .toList();
    }
}
