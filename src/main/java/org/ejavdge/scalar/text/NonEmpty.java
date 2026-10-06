package org.ejavdge.scalar.text;

import org.ejavdge.error.InvariantViolation;

/**
 * A {@link Text} decorator that requires the underlying text content to be
 * non-empty.
 * <p>
 * This class wraps another {@link Text} instance and enforces an invariant:
 * when {@link #content()} is called, the underlying content is materialized
 * and, if it turns out to be an empty string, an {@link InvariantViolation} is
 * thrown. If the content is non-empty, it is returned unchanged.
 * <p>
 * The error message used when the content is empty can be customized via a
 * constructor. The default message is {@code "Text is empty."}, but callers
 * may supply a more descriptive message suited to the specific context, such
 * as indicating which value was expected to be present.
 * <p>
 * This is useful whenever the absence of text would indicate a violation of
 * the application's assumptions — for example, when extracting a required
 * value (such as an identifier, name, or token) from a page or file.
 */
public final class NonEmpty implements Text {

    /**
     * The underlying text that must be non-empty.
     */
    private final Text origin;

    /**
     * The message to include in the exception if the content is empty.
     */
    private final Text message;

    /**
     * Creates a decorator that requires the given text to be non-empty, using
     * the default error message.
     * <p>
     * The default message is {@code "Text is empty."}.
     *
     * @param x the text that must contain at least one character
     */
    public NonEmpty(final Text x) {
        this(x, new Text.Of("Text is empty."));
    }

    /**
     * Creates a decorator that requires the given text to be non-empty, using
     * the specified error message.
     *
     * @param x the text that must contain at least one character
     * @param m the message to use if the content is empty
     */
    public NonEmpty(final Text x, final Text m) {
        this.origin = x;
        this.message = m;
    }

    /**
     * Returns the content of the underlying text, requiring it to be
     * non-empty.
     * <p>
     * The underlying content is materialized via {@link Text#content()}. If it
     * is an empty string, an {@link InvariantViolation} is thrown with the
     * configured message. Otherwise, the content is returned unchanged.
     *
     * @return the non-empty content of the underlying text
     * @throws InvariantViolation if the underlying content is empty or if an
     *         invariant is violated while materializing it or the message
     */
    @Override
    public String content() throws InvariantViolation {
        final String x = this.origin.content();
        if (x.isEmpty()) {
            throw new InvariantViolation(this.message.content());
        }
        return x;
    }
}
