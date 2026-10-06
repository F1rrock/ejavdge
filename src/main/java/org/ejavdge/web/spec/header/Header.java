package org.ejavdge.web.spec.header;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.bytes.Concat;
import org.ejavdge.scalar.bytes.Utf8;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.scalar.text.Stencil;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.scalar.text.TextOfNum;
import org.ejavdge.web.spec.Terminator;

/**
 * A {@link Bytes} representing a single HTTP header line.
 * <p>
 * An HTTP header is a {@code Name: Value} pair terminated by a line break.
 * This class builds such a line from a name and a value, formatting them as
 * {@code "<name>: <value>"} and appending a {@link Terminator} so that the
 * resulting bytes are ready to be included in a request or response
 * header block.
 * <p>
 * The value can be supplied either as a {@link Text} or as a {@link Num};
 * in the latter case it is converted to its decimal representation via
 * {@link TextOfNum}. A precomputed byte sequence can also be wrapped
 * directly, which is useful when reconstructing a header block from an
 * existing payload.
 */
public final class Header implements Bytes {

    /**
     * The underlying byte content of the header line.
     */
    private final Bytes src;

    /**
     * Creates a header line from the given name and numeric value.
     * <p>
     * The numeric value is converted to text via {@link TextOfNum} and
     * then formatted as {@code "<name>: <value>"} with a trailing line
     * break.
     *
     * @param n the header name
     * @param v the header value as a number
     */
    public Header(final Text n, final Num v) {
        this(n, new TextOfNum(v));
    }

    /**
     * Creates a header line from the given name and text value.
     * <p>
     * The name and value are formatted as {@code "<name>: <value>"} using
     * a {@link Stencil}, encoded as UTF-8 bytes, and terminated with a
     * line break via {@link Terminator}.
     *
     * @param n the header name
     * @param v the header value as text
     */
    public Header(final Text n, final Text v) {
        this(
            new Concat(
                new Utf8(
                    new Stencil(
                        new Text.Of("%s: %s"),
                        n, v
                    )
                ),
                new Terminator()
            )
        );
    }

    /**
     * Creates a header line from precomputed byte content.
     * <p>
     * This constructor is intended for cases where the full header line
     * (including the terminating line break) has already been assembled,
     * for example when reconstructing a header block from an existing
     * request.
     *
     * @param bs the raw bytes of the header line
     */
    public Header(final Bytes bs) {
        this.src = bs;
    }

    /**
     * Returns the raw bytes of this header line, including the trailing
     * line break.
     *
     * @return the header line as a byte array
     * @throws InvariantViolation if an invariant is violated while
     *         materializing the header
     */
    @Override
    public byte[] content() throws InvariantViolation {
        return this.src.content();
    }
}
