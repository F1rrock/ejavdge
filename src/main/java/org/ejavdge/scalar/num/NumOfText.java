package org.ejavdge.scalar.num;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

/**
 * A {@link Num} that parses its value from a decimal string.
 * <p>
 * This class implements {@link Num} by interpreting the content of a given
 * {@link Text} as a base-10 integer. The parsing is performed using
 * {@link Integer#parseInt(String)}, so the string may contain digits
 * {@code 0-9} and an optional leading sign. Whitespace is not allowed.
 * <p>
 * If the string cannot be parsed as a decimal integer, an
 * {@link InvariantViolation} is thrown with a message indicating the invalid
 * input, and the original {@link NumberFormatException} is attached as the
 * cause.
 * <p>
 * This is the most common way to turn extracted textual content (such as an
 * HTML attribute value or a segment of a report) into a numeric domain value.
 * For hexadecimal input, use {@link NumOfHex} instead.
 */
public final class NumOfText implements Num {

    /**
     * The source text containing the decimal representation of the number.
     */
    private final Text src;

    /**
     * Creates a decimal number from the given string.
     *
     * @param s the decimal string to parse
     */
    public NumOfText(final String s) {
        this(new Text.Of(s));
    }

    /**
     * Creates a decimal number from the given text.
     *
     * @param t the text whose content will be parsed as a decimal number
     */
    public NumOfText(final Text t) {
        this.src = t;
    }

    /**
     * Returns the numeric value parsed from the underlying text as a decimal
     * number.
     * <p>
     * The text content is obtained via {@link Text#content()} and parsed as a
     * base-10 integer. If the content is not a valid decimal number, an
     * {@link InvariantViolation} is thrown with the offending string included
     * in the message and the underlying {@link NumberFormatException} as the
     * cause.
     *
     * @return the integer value represented by the decimal string
     * @throws InvariantViolation if the underlying text cannot be parsed as a
     *         decimal number
     */
    @Override
    public int value() throws InvariantViolation {
        final var s = this.src.content();
        try {
            return Integer.parseInt(s);
        } catch (final NumberFormatException e) {
            throw new InvariantViolation(
                s + " is not a valid number.\n",
                e
            );
        }
    }
}
