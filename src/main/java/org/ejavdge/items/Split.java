package org.ejavdge.items;

import org.ejavdge.error.InvariantViolation;

import java.util.List;
import java.util.function.Predicate;

/**
 * An {@link Items} view that splits a source collection into groups of elements
 * separated by elements matching a given predicate.
 * <p>
 * This class wraps a source collection and a predicate, and exposes a
 * collection of collections. Each inner collection contains the elements
 * between two consecutive occurrences of an element that satisfies the
 * predicate. The separator elements themselves are not included in the result.
 * <p>
 * The split is performed recursively:
 * <ol>
 *   <li>If the source collection is empty, the result is an empty list.</li>
 *   <li>Otherwise, the first group consists of all elements up to (but not
 *       including) the first element that satisfies the predicate.</li>
 *   <li>The remaining elements (those after the first separator) are split
 *       again using the same predicate, and the resulting groups are appended
 *       to the first group.</li>
 * </ol>
 * <p>
 * This is useful for parsing structured text where sections are separated by
 * known markers. For example, a sequence of lines can be split into groups
 * using a predicate that matches a line equal to {@code "Input"}, yielding one
 * group per input block.
 *
 * @param <T> the type of elements in the source collection
 */
public final class Split<T> implements Items<Items<T>> {

    /**
     * The predicate that identifies the separator elements.
     */
    private final Predicate<T> predicate;

    /**
     * The source collection to be split.
     */
    private final Items<T> src;

    /**
     * Creates a split view over the given source collection using the given
     * predicate as the separator.
     *
     * @param f  the predicate that identifies separator elements
     * @param xs the source collection to split
     */
    public Split(final Predicate<T> f, final Items<T> xs) {
        this.predicate = f;
        this.src = xs;
    }

    /**
     * Returns the list of groups obtained by splitting the source collection.
     * <p>
     * Each group consists of the elements between two consecutive separator
     * elements. Separators themselves are excluded from the result. If the
     * source collection is empty, an empty list is returned. If the source
     * contains no separator, a single group containing all elements is
     * returned.
     *
     * @return the list of groups obtained by splitting the source collection
     * @throws InvariantViolation if an invariant is violated while
     *         materializing the source collection or building the groups
     */
    @Override
    public List<Items<T>> contents() throws InvariantViolation {
        final var xs = this.src.contents();
        if (xs.isEmpty()) {
            return List.of();
        }
        return new Joint<>(
            new Items.Of<>(
                new OnlyUntil<>(
                    this.predicate,
                    new Items.Of<>(xs)
                )
            ),
            new Split<>(
                this.predicate,
                new OnlyAfter<>(
                    this.predicate,
                    new Items.Of<>(xs)
                )
            )
        ).contents();
    }
}
