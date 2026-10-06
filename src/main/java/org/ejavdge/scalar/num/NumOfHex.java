package org.ejavdge.scalar.num;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

/**
 * A {@link Num} that parses its value from a hexadecimal string.
 * <p>
 * This class implements {@link Num} by interpreting the content of a given
 * {@link Text} as a hexadecimal integer. The parsing is performed using
 * {@link Integer#parseInt(String, int)} with a radix of {@code 16}, so the
 * string may contain digits {@code 0-9} and letters {@code A-F} (case-insensitive),
 * optionally preceded by a sign. Whitespace is not allowed.
 * <p>
 * If the string cannot be parsed as a hexadecimal number, an
 * {@link InvariantViolation} is thrown with a message indicating the invalid
 * input, and the original {@link NumberFormatException} is attached as the
 * cause.
 * <p>
 * This is useful when numeric values are encoded in hexadecimal form, such as
 * color codes, identifiers, or compact representations of integers.
 */
public final class NumOfHex implements Num {

    /**
     * The source text containing the hexadecimal representation of the number.
     */
    private final Text src;

    /**
     * Creates a hexadecimal number from the given string.
     *
     * @param s the hexadecimal string to parse
     */
    public NumOfHex(final String s) {
        this(new Text.Of(s));
    }

    /**
     * Creates a hexadecimal number from the given text.
     *
     * @param t the text whose content will be parsed as a hexadecimal number
     */
    public NumOfHex(final Text t) {
        this.src = t;
    }

    /**
     * Returns the numeric value parsed from the underlying text as a
     * hexadecimal number.
     * <p>
     * The text content is obtained via {@link Text#content()} and parsed as a
     * base-16 integer. If the content is not a valid hexadecimal number, an
     * {@link InvariantViolation} is thrown with the offending string included
     * in the message and the underlying {@link NumberFormatException} as the
     * cause.
     *
     * @return the integer value represented by the hexadecimal string
     * @throws InvariantViolation if the underlying text cannot be parsed as a
     *         hexadecimal number
     */
    @Override
    public int value() throws InvariantViolation {
        final var s = this.src.content();
        try {
            return Integer.parseInt(s, 16);
        } catch (final NumberFormatException e) {
            throw new InvariantViolation(
                s + " is not a valid hex number.\n",
                e
            );
        }
    }
}
