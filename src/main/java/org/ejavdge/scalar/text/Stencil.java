package org.ejavdge.scalar.text;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.items.Items;

import java.util.IllegalFormatException;

/**
 * A {@link Text} that substitutes values into a format template.
 * <p>
 * This class implements {@link Text} and represents a parameterized string,
 * similar in spirit to {@link String#format(String, Object...)}. It holds a
 * template {@link Text} (containing format specifiers such as {@code %s},
 * {@code %d}, and so on) and a collection of {@link Text} arguments. When
 * {@link #content()} is called, the template is materialized, each argument is
 * materialized, and the arguments are substituted into the template using
 * {@link String#format}.
 * <p>
 * The arguments can be provided either as varargs of {@link Text}, or as an
 * {@link Items} collection of text values. Because the format specifiers are
 * resolved by {@code String.format}, the usual rules of the
 * {@link java.util.Formatter} apply, including support for indexed arguments,
 * width, precision, and conversion flags.
 * <p>
 * If the template contains an invalid format string, or if the arguments do
 * not match the specifiers (for example, a {@code %d} specifier without a
 * numeric argument), a {@link IllegalFormatException} is thrown by the
 * underlying formatter. This class catches that exception and rethrows it as
 * an {@link InvariantViolation} with the message {@code "Incorrect stencil.\n"}
 * and the original exception as the cause.
 * <p>
 * This is a fundamental building block for constructing dynamic text — for
 * example, generating XPath expressions, request bodies, or command lines —
 * without manually concatenating strings and arguments.
 */
public final class Stencil implements Text {

    /**
     * The template text containing format specifiers.
     */
    private final Text template;

    /**
     * The collection of text arguments to substitute into the template.
     */
    private final Items<Text> xs;

    /**
     * Creates a stencil from the given template and arguments.
     *
     * @param t  the template containing format specifiers
     * @param xs the arguments to substitute into the template
     */
    public Stencil(final Text t, final Text ...xs) {
        this(t, new Items.Of<>(xs));
    }

    /**
     * Creates a stencil from the given template and collection of arguments.
     *
     * @param t  the template containing format specifiers
     * @param xs the collection of arguments to substitute into the template
     */
    public Stencil(final Text t, final Items<Text> xs) {
        this.template = t;
        this.xs = xs;
    }

    /**
     * Returns the result of substituting the arguments into the template.
     * <p>
     * The template and all arguments are materialized via
     * {@link Text#content()}, and the resulting strings are passed to
     * {@link String#format} for substitution. If the template is not a valid
     * format string, or if the arguments do not match the format specifiers, an
     * {@link InvariantViolation} is thrown wrapping the underlying
     * {@link IllegalFormatException}.
     *
     * @return the formatted text
     * @throws InvariantViolation if the template cannot be formatted with the
     *         given arguments, or if an invariant is violated while retrieving
     *         the template or argument contents
     */
    @Override
    public String content() throws InvariantViolation {
        try {
            return String.format(
                this.template.content(),
                this.xs.contents()
                    .stream()
                    .map(Text::content)
                    .toArray()
            );
        } catch (final IllegalFormatException e) {
            throw new InvariantViolation(
                "Incorrect stencil.\n",
                e
            );
        }
    }
}
