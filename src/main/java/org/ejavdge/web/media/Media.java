package org.ejavdge.web.media;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

/**
 * A contract for a web media type that can accumulate named entries and
 * materialize them into a final value of type {@code T}.
 * <p>
 * A {@code Media} represents the target of a web request or response — such as
 * a set of form fields, a collection of cookies, or any other name-value-based
 * payload. Implementations provide two operations:
 * <ul>
 *   <li>{@link #with(Text, Text)} – returns a new media instance that includes
 *       an additional named entry, preserving immutability and allowing entries
 *       to be composed functionally;</li>
 *   <li>{@link #content()} – materializes the accumulated entries into a final
 *       value of type {@code T}, such as a string, a byte array, or a
 *       structured collection.</li>
 * </ul>
 * <p>
 * Media instances are the destination of {@link
 * org.ejavdge.web.context.Context#imprint(Media)} operations: a context applies
 * its data to a media by repeatedly calling {@link #with(Text, Text)}, and the
 * resulting media is then materialized via {@link #content()}.
 *
 * @param <T> the type of the final value produced by this media
 */
public interface Media<T> {

    /**
     * Returns a new media instance that includes the given named entry in
     * addition to all entries already present.
     * <p>
     * Implementations must not modify the current instance; instead, they must
     * return a new media value that represents the combination of the existing
     * entries and the new one. This allows media values to be composed in a
     * functional style, for example when a {@link
     * org.ejavdge.web.context.Context} is imprinted onto a media.
     *
     * @param n the name of the entry to add
     * @param v the value of the entry to add
     * @return a new media instance containing the additional entry
     * @throws InvariantViolation if an invariant is violated while adding the
     *         entry
     */
    Media<T> with(final Text n, final Text v) throws InvariantViolation;

    /**
     * Returns the final value produced by this media.
     * <p>
     * Calling this method materializes all accumulated entries into a single
     * value of type {@code T}, such as a string, a byte array, or a collection.
     * Implementations may perform encoding, joining, or other transformations
     * as part of this operation.
     *
     * @return the materialized content of this media
     * @throws InvariantViolation if an invariant is violated while materializing
     *         the content
     */
    T content() throws InvariantViolation;
}
