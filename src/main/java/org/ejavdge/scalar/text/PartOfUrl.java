package org.ejavdge.scalar.text;

import org.ejavdge.error.InvariantViolation;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * A {@link Text} decorator that URL-encodes its content.
 * <p>
 * This class wraps another {@link Text} instance and, when {@link #content()}
 * is called, returns the underlying string encoded for safe inclusion in a URL
 * query or path segment. The encoding is performed using
 * {@link URLEncoder#encode(String, java.nio.charset.Charset)} with the UTF-8
 * charset.
 * <p>
 * The {@code URLEncoder} normally encodes spaces as {@code "+"} (the
 * {@code application/x-www-form-urlencoded} convention). Since this is not
 * always desirable when constructing URLs (where {@code "%20"} is the more
 * standard representation of a space), this class replaces any {@code "+"}
 * characters produced by the encoder with {@code "%20"}.
 * <p>
 * This is useful whenever text extracted from the environment (such as a
 * problem name or a search query) needs to be embedded into a URL without
 * breaking its structure.
 */
public final class PartOfUrl implements Text {

    /**
     * The underlying text whose content is URL-encoded.
     */
    private final Text origin;

    /**
     * Creates a URL-encoded view over the given text.
     *
     * @param origin the text to URL-encode
     */
    public PartOfUrl(final Text origin) {
        this.origin = origin;
    }

    /**
     * Returns the URL-encoded content of the underlying text.
     * <p>
     * The underlying content is materialized via {@link Text#content()} and
     * encoded using {@link URLEncoder} with UTF-8. Any {@code "+"} characters
     * produced by the encoder (used for spaces) are replaced with
     * {@code "%20"} to conform to typical URL encoding conventions.
     *
     * @return the URL-encoded content as a string
     * @throws InvariantViolation if an invariant is violated while retrieving
     *         the underlying content
     */
    @Override
    public String content() throws InvariantViolation {
        return URLEncoder.encode(
            this.origin.content(),
            StandardCharsets.UTF_8
        ).replace("+", "%20");
    }
}
