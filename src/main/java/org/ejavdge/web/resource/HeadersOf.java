package org.ejavdge.web.resource;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;

import java.nio.charset.StandardCharsets;

/**
 * A {@link Bytes} decorator that extracts the initial header block of an
 * HTTP response, including the status line.
 * <p>
 * The first {@code \r\n\r\n} separates the header block from the body.
 * ISO-8859-1 is used for its one-to-one byte mapping while finding the
 * boundary, so the returned header bytes are unchanged.
 */
public final class HeadersOf implements Bytes {

    /**
     * The full HTTP response whose header block is extracted.
     */
    private final Bytes origin;

    /**
     * Creates a header extractor over the given full HTTP response.
     *
     * @param b the raw HTTP response bytes
     */
    public HeadersOf(final Bytes b) {
        this.origin = b;
    }

    /**
     * Returns the response bytes before the first {@code \r\n\r\n} separator.
     *
     * @return the status line and header fields, without the separator or body
     * @throws InvariantViolation if the separator is missing or the response
     *         bytes cannot be obtained
     */
    @Override
    public byte[] content() throws InvariantViolation {
        final var full = new String(this.origin.content(), StandardCharsets.ISO_8859_1);
        final int idx = full.indexOf("\r\n\r\n");
        if (idx < 0) {
            throw new InvariantViolation("HTTP response has no header terminator.");
        }
        return full.substring(0, idx).getBytes(StandardCharsets.ISO_8859_1);
    }
}
