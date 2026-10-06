package org.ejavdge.scalar.text;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.num.Num;

/**
 * An adapter that exposes a {@link Num} numeric value as a {@link Text}.
 * <p>
 * This class bridges the numeric and textual abstractions: it holds a
 * {@link Num} instance and, when {@link #content()} is called, materializes the
 * number via {@link Num#value()} and converts it to its decimal string
 * representation using {@link String#valueOf(int)}.
 * <p>
 * This is useful whenever a numeric value needs to be embedded in a text, such
 * as when substituting numbers into a {@link Stencil} template, building a URL
 * with a numeric parameter, or formatting a report. The numeric value can be
 * supplied either as a plain {@code int} or as a {@link Num} instance, allowing
 * the number to be computed lazily if desired.
 */
public final class TextOfNum implements Text {

    /**
     * The underlying numeric value whose string representation is exposed.
     */
    private final Num src;

    /**
     * Creates a text view over the given integer value.
     *
     * @param n the integer value to expose as text
     */
    public TextOfNum(final int n) {
        this(new Num.Of(n));
    }

    /**
     * Creates a text view over the given numeric value.
     *
     * @param n the numeric value to expose as text
     */
    public TextOfNum(final Num n) {
        this.src = n;
    }

    /**
     * Returns the decimal string representation of the underlying numeric
     * value.
     * <p>
     * The number is materialized via {@link Num#value()} and converted to a
     * string using standard decimal notation.
     *
     * @return the numeric value as a decimal string
     * @throws InvariantViolation if an invariant is violated while retrieving
     *         the numeric value
     */
    @Override
    public String content() throws InvariantViolation {
        return String.valueOf(this.src.value());
    }
}
