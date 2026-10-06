package org.ejavdge.web.media;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

/**
 * A {@link Media} decorator that lifts an inner media value into the outer
 * media position, allowing it to be consumed by a {@link
 * org.ejavdge.web.context.Context} during composition.
 * <p>
 * This class is used internally by {@link
 * org.ejavdge.web.context.Union} to combine two contexts. When a union is
 * imprinted onto a media, the left context is applied first, producing an
 * intermediate value. That intermediate value is then wrapped in a {@code Lift}
 * so that it can be passed to the right context as if it were itself a media.
 * This is what allows the two contexts to be applied in sequence, each
 * operating on the result of the previous one.
 * <p>
 * The {@link #with(Text, Text)} method delegates to the underlying media and
 * re-wraps the result in a new {@code Lift}, preserving the lifting behavior.
 * The {@link #content()} method unwraps the outer layer and returns the
 * original media value, so that the right context can continue processing it.
 *
 * @param <T> the type of value produced by the underlying media
 */
public final class Lift<T> implements Media<Media<T>> {

    /**
     * The underlying media whose value is being lifted.
     */
    private final Media<T> src;

    /**
     * Creates a lift over the given media.
     *
     * @param m the media whose value is being lifted
     */
    public Lift(final Media<T> m) {
        this.src = m;
    }

    /**
     * Returns a new lift that delegates the addition of a named entry to the
     * underlying media and re-wraps the result.
     * <p>
     * The name-value pair is passed through to the underlying media via
     * {@link Media#with(Text, Text)}, producing a new media value. That value
     * is then wrapped in a new {@code Lift} so that the lifting behavior is
     * preserved for further composition.
     *
     * @param n the name of the entry to add
     * @param v the value of the entry to add
     * @return a new {@code Lift} wrapping the extended underlying media
     * @throws InvariantViolation if an invariant is violated while adding the
     *         entry
     */
    @Override
    public Lift<T> with(final Text n, final Text v) throws InvariantViolation {
        return new Lift<>(this.src.with(n, v));
    }

    /**
     * Unwraps the lift and returns the underlying media value.
     * <p>
     * This method exposes the original media that was lifted, allowing the
     * outer context in a composition to continue processing it.
     *
     * @return the underlying media value
     * @throws InvariantViolation if an invariant is violated while retrieving
     *         the value
     */
    @Override
    public Media<T> content() throws InvariantViolation {
        return this.src;
    }
}
