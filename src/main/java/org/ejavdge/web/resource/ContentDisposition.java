package org.ejavdge.web.resource;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.text.*;

/**
 * The disposition type in an HTTP response's {@code Content-Disposition}
 * header.
 * <p>
 * Only the header block is decoded as UTF-8 and searched. The header name is
 * matched without regard to case. The returned type keeps its original case
 * and excludes parameters such as {@code filename}.
 */
public final class ContentDisposition implements Text {

    /**
     * The source of the extracted disposition type.
     */
    private final Text origin;

    /**
     * Creates a disposition type extractor for the given response.
     *
     * @param src the full HTTP response bytes
     */
    public ContentDisposition(final Bytes src) {
        this.origin = new TextAbout(
            "content disposition",
            new Match(
                new Utf8Text(
                    new HeadersOf(src)
                ),
                new Concat(
                    "(?im)(?<=^Content-Disposition:[ \\t]*)",
                    "[!#$%&'*+.^_`|~0-9A-Za-z-]+(?=[ \\t]*(?:;|$))"
                )
            )
        );
    }

    /**
     * Returns the disposition type, such as {@code attachment} or {@code inline}.
     *
     * @return the disposition type without its parameters
     * @throws InvariantViolation if the headers cannot be read or no valid
     *         disposition type can be extracted
     */
    @Override
    public String content() throws InvariantViolation {
        return this.origin.content();
    }
}
