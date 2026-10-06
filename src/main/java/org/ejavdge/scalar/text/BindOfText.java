package org.ejavdge.scalar.text;

import org.ejavdge.error.InvariantViolation;

import java.util.function.Function;

/**
 * A monadic bind operation for {@link Text}.
 * <p>
 * This class implements {@link Text} and represents a deferred transformation
 * of textual content. It holds an original {@code Text} instance and a function
 * that takes the original string and produces a new {@code Text} instance. When
 * {@link #content()} is called, the original text is materialized, passed to the
 * binding function, and the contents of the resulting {@code Text} are returned.
 * <p>
 * This allows chaining operations on textual data where each step depends on
 * the actual content of the previous step, similar to {@code flatMap} on
 * streams. It is typically used to build complex text transformations in a
 * declarative manner, such as fetching a page and then using its content to
 * construct another text that extracts a specific fragment from it.
 */
public final class BindOfText implements Text {

    /**
     * The original text used as input to the binding function.
     */
    private final Text origin;

    /**
     * The function that transforms the original string into a new
     * {@link Text} instance.
     */
    private final Function<String, Text> binding;

    /**
     * Creates a new bound text from the given original text and binding
     * function.
     *
     * @param t the original text
     * @param f the function that maps the original text's content to a new
     *          {@code Text} instance
     */
    public BindOfText(final Text t, final Function<String, Text> f) {
        this.origin = t;
        this.binding = f;
    }

    /**
     * Returns the content of the {@code Text} produced by applying the binding
     * function to the original text content.
     * <p>
     * The original content is obtained via {@link Text#content()}, passed to
     * the binding function, and the content of the resulting {@code Text} is
     * returned.
     *
     * @return the resulting text content
     * @throws InvariantViolation if an invariant is violated while retrieving
     *         the original content or evaluating the binding
     */
    @Override
    public String content() throws InvariantViolation {
        return this.binding.apply(
            this.origin.content()
        ).content();
    }
}
