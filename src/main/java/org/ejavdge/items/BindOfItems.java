package org.ejavdge.items;

import org.ejavdge.error.InvariantViolation;

import java.util.List;
import java.util.function.Function;

/**
 * A monadic bind operation for {@link Items}.
 * <p>
 * This class implements {@link Items} and represents a deferred transformation
 * of a collection. It holds an original {@code Items} instance and a function
 * that takes the original list of elements and produces a new {@code Items}
 * instance. When {@link #contents()} is called, the original list is obtained,
 * passed to the binding function, and the contents of the resulting
 * {@code Items} are returned.
 * <p>
 * This allows chaining operations on collections where each step depends on the
 * full result of the previous step, similar to {@code flatMap} on streams. It
 * is typically used to build complex item transformations in a declarative
 * manner.
 *
 * @param <T> the type of elements in the collection
 */
public final class BindOfItems<T> implements Items<T> {

    /**
     * The original collection whose contents are used as input to the binding
     * function.
     */
    private final Items<T> origin;

    /**
     * The function that transforms the original list of elements into a new
     * collection.
     */
    private final Function<List<T>, Items<T>> binding;

    /**
     * Creates a new bound collection from the given original collection and
     * binding function.
     *
     * @param xs the original collection
     * @param f  the function that maps the original list of elements to a new
     *           collection
     */
    public BindOfItems(final Items<T> xs, final Function<List<T>, Items<T>> f) {
        this.origin = xs;
        this.binding = f;
    }

    /**
     * Returns the contents of the collection produced by applying the binding
     * function to the original collection's contents.
     *
     * @return the list of elements after applying the binding function
     * @throws InvariantViolation if an invariant is violated while retrieving
     *         the contents
     */
    @Override
    public List<T> contents() throws InvariantViolation {
        return this.binding.apply(
            this.origin.contents()
        ).contents();
    }
}
