package org.ejavdge.web.resource;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.text.Lowers;
import org.ejavdge.scalar.text.Match;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.scalar.text.TextAbout;
import org.ejavdge.scalar.text.Utf8Text;

/**
 * A {@link Text} that represents the value of the {@code Transfer-Encoding}
 * header in an HTTP response.
 * <p>
 * This class extracts the value of the {@code Transfer-Encoding} header from a
 * raw HTTP response, which is expected to contain the response headers. The
 * extraction is performed as follows:
 * <ol>
 *   <li>The raw bytes are decoded as UTF-8 text via {@link Utf8Text};</li>
 *   <li>The text is converted to lowercase via {@link Lowers}, so that the
 *       header lookup is case-insensitive regardless of how the server
 *       capitalized the header name;</li>
 *   <li>The value of the header is extracted using the regular expression
 *       {@code (?<=transfer-encoding:\s*)\S+}, which matches the first
 *       non-whitespace token following the header name and optional
 *       whitespace.</li>
 * </ol>
 * <p>
 * The extracted value is wrapped in a {@link TextAbout} with the subject
 * {@code "transfer-encoding"}, so any failure while materializing the value is
 * reported with a meaningful message.
 * <p>
 * This class is used by {@link org.ejavdge.web.driver.jdk.socket.body.ChunkPolicy}
 * to detect whether an HTTP response uses chunked transfer encoding, which
 * determines how the response body should be decoded.
 */
public final class TransferEncoding implements Text {

    /**
     * The underlying textual value of the {@code Transfer-Encoding} header.
     */
    private final Text origin;

    /**
     * Creates a transfer-encoding value by extracting it from the given raw
     * response bytes, which are expected to contain the HTTP headers.
     *
     * @param src the raw response bytes containing the
     *            {@code Transfer-Encoding} header
     */
    public TransferEncoding(final Bytes src) {
        this.origin = new TextAbout(
            "transfer-encoding",
            new Match(
                new Lowers(
                    new Utf8Text(src)
                ),
                new Text.Of(
                    "(?<=transfer-encoding:\\s*)\\S+"
                )
            )
        );
    }

    /**
     * Returns the value of the {@code Transfer-Encoding} header.
     *
     * @return the transfer encoding value as a string
     * @throws InvariantViolation if the header is missing or if an invariant is
     *         violated while materializing the value
     */
    @Override
    public String content() throws InvariantViolation {
        return this.origin.content();
    }
}
