package org.ejavdge.web.spec;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.bytes.Utf8;
import org.ejavdge.scalar.text.Text;

/**
 * A {@link Bytes} that represents the standard HTTP line terminator,
 * {@code CRLF} ({@code "\r\n"}).
 * <p>
 * The HTTP protocol uses {@code CRLF} to separate header lines from one
 * another and to mark the end of the header block (a blank line, i.e. two
 * consecutive {@code CRLF}s). This class encapsulates that two-byte
 * sequence as a reusable {@code Bytes} value, so that it can be
 * concatenated into request and response messages without hard-coding the
 * literal in every call site.
 * <p>
 * A {@code Terminator} is used in several places within the HTTP spec
 * hierarchy: as the line ending appended by {@link
 * org.ejavdge.web.spec.header.Header}, as the separator inserted between a
 * header block and a body by {@link
 * org.ejavdge.web.spec.body.WithBody}, and as the final terminator that
 * closes a complete request in {@link Request}.
 */
public final class Terminator implements Bytes {

    /**
     * The underlying byte content of the terminator.
     */
    private final Bytes origin;

    /**
     * Creates a terminator representing the standard HTTP line ending
     * {@code CRLF} ({@code "\r\n"}), encoded as UTF-8 bytes.
     */
    public Terminator() {
        this.origin = new Utf8(
            new Text.Of("\r\n")
        );
    }

    /**
     * Returns the raw bytes of the terminator.
     *
     * @return the two-byte sequence {@code CRLF} as a byte array
     * @throws InvariantViolation if an invariant is violated while
     *         materializing the terminator
     */
    @Override
    public byte[] content() throws InvariantViolation {
        return this.origin.content();
    }
}
