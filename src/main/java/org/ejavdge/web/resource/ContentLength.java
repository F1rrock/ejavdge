package org.ejavdge.web.resource;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.scalar.num.NumAbout;
import org.ejavdge.scalar.num.NumOfText;
import org.ejavdge.scalar.text.Lowers;
import org.ejavdge.scalar.text.Match;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.scalar.text.Utf8Text;

/**
 * A {@link Num} that represents the value of the {@code Content-Length} header
 * in an HTTP response.
 * <p>
 * This class extracts the numeric value of the {@code Content-Length} header
 * from a raw HTTP response, which is expected to contain the response headers.
 * The extraction is performed as follows:
 * <ol>
 *   <li>The raw bytes are decoded as UTF-8 text via {@link Utf8Text};</li>
 *   <li>The text is converted to lowercase via {@link Lowers}, so that the
 *       header lookup is case-insensitive regardless of how the server
 *       capitalized the header name;</li>
 *   <li>The value of the header is extracted using the regular expression
 *       {@code (?<=content-length:\s*)\d+}, which matches the sequence of digits
 *       following the header name and optional whitespace;</li>
 *   <li>The extracted string is parsed as a decimal integer via
 *       {@link NumOfText} and labelled with the subject {@code "content-length"}
 *       via {@link NumAbout}, so that any failure while materializing the value
 *       is reported with a meaningful message.</li>
 * </ol>
 * <p>
 * This class is used by the JDK socket-based web driver to determine the length
 * of the response body when it is bounded by a {@code Content-Length} header,
 * as opposed to being encoded with chunked transfer encoding.
 */
public final class ContentLength implements Num {

    /**
     * The underlying numeric value extracted from the header.
     */
    private final Num origin;

    /**
     * Creates a content-length value by extracting it from the given raw
     * response bytes, which are expected to contain the HTTP headers.
     *
     * @param src the raw response bytes containing the {@code Content-Length}
     *            header
     */
    public ContentLength(final Bytes src) {
        this.origin = new NumAbout(
            "content-length",
            new NumOfText(
                new Match(
                    new Lowers(
                        new Utf8Text(src)
                    ),
                    new Text.Of(
                        "(?<=content-length:\\s*)\\d+"
                    )
                )
            )
        );
    }

    /**
     * Returns the numeric value of the {@code Content-Length} header.
     *
     * @return the content length as an integer
     * @throws InvariantViolation if the header is missing or cannot be parsed
     *         as a decimal number
     */
    @Override
    public int value() throws InvariantViolation {
        return this.origin.value();
    }
}
