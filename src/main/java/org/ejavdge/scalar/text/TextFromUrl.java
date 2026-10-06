package org.ejavdge.scalar.text;

import org.ejavdge.error.InvariantViolation;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

/**
 * A {@link Text} decorator that URL-decodes its content.
 * <p>
 * This class wraps another {@link Text} instance and, when {@link #content()}
 * is called, returns the underlying string decoded from
 * {@code application/x-www-form-urlencoded} form. The decoding is performed
 * using {@link URLDecoder#decode(String, java.nio.charset.Charset)} with the
 * UTF-8 charset, so both {@code "+"} (representing a space) and {@code "%XX"}
 * escape sequences are converted back to their original characters.
 * <p>
 * This is useful for reversing the encoding applied by
 * {@link PartOfUrl} or by a remote server, such as when extracting a file name
 * or other value from a URL-encoded link.
 * <p>
 * If the underlying content is not a valid URL-encoded string (for example, it
 * contains a malformed percent-escape sequence), {@link URLDecoder} throws an
 * {@link IllegalArgumentException}. This class catches that exception and
 * rethrows it as an {@link InvariantViolation} using a configurable error
 * message. The default message is {@code "There is no legal url."}, but callers
 * can supply a more context-specific message via the second constructor.
 */
public final class TextFromUrl implements Text {

    /**
     * The underlying text whose content is URL-decoded.
     */
    private final Text origin;

    /**
     * The message to include in the exception if the content cannot be decoded.
     */
    private final Text message;

    /**
     * Creates a URL-decoded view over the given text, using the default error
     * message.
     * <p>
     * The default error message is {@code "There is no legal url."}.
     *
     * @param u the URL-encoded text to decode
     */
    public TextFromUrl(final Text u) {
        this(u, new Text.Of("There is no legal url."));
    }

    /**
     * Creates a URL-decoded view over the given text, using the specified error
     * message.
     *
     * @param u the URL-encoded text to decode
     * @param m the message to use if the content cannot be decoded
     */
    public TextFromUrl(final Text u, final Text m) {
        this.origin = u;
        this.message = m;
    }

    /**
     * Returns the URL-decoded content of the underlying text.
     * <p>
     * The underlying content is materialized via {@link Text#content()} and
     * decoded using {@link URLDecoder} with UTF-8. If the content is not a
     * valid URL-encoded string, an {@link InvariantViolation} is thrown with
     * the configured message and the underlying
     * {@link IllegalArgumentException} as the cause.
     *
     * @return the URL-decoded content as a string
     * @throws InvariantViolation if the underlying content cannot be decoded
     *         or if an invariant is violated while retrieving it
     */
    @Override
    public String content() throws InvariantViolation {
        try {
            return URLDecoder.decode(
                this.origin.content(),
                StandardCharsets.UTF_8
            );
        } catch (final IllegalArgumentException e) {
            throw new InvariantViolation(this.message.content(), e);
        }
    }
}
