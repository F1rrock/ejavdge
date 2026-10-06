package org.ejavdge.web.driver.jdk.stream;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;

import java.io.ByteArrayOutputStream;
import java.util.stream.IntStream;

/**
 * A {@link Bytes} view over an {@link IntStream} of byte values.
 * <p>
 * This class adapts a stream of {@code int} values — each expected to represent
 * a single byte in the range {@code 0} to {@code 255} — into a raw byte array.
 * It is the streaming counterpart of {@link BytesOfLine}: while
 * {@code BytesOfLine} is typically used for a single line, {@code BytesOfStream}
 * is intended for arbitrary streams of byte values, such as the entire body of
 * an HTTP response.
 * <p>
 * The conversion is performed by collecting the stream into a
 * {@link ByteArrayOutputStream}, writing each {@code int} value as a single
 * byte. Because {@link ByteArrayOutputStream#write(int)} writes only the
 * low-order eight bits of its argument, the caller is expected to supply values
 * that already fit in the byte range.
 * <p>
 * This class is used internally by the socket-based web driver to convert
 * decoded response bodies into byte arrays that can then be concatenated,
 * memoized, or otherwise processed as {@link Bytes}.
 */
public final class BytesOfStream implements Bytes {

    /**
     * The underlying stream of byte values.
     */
    private final IntStream src;

    /**
     * Creates a byte sequence from the given stream of byte values.
     *
     * @param s the stream of byte values, each expected to fit in the range
     *          {@code 0} to {@code 255}
     */
    public BytesOfStream(final IntStream s) {
        this.src = s;
    }

    /**
     * Returns the byte content obtained by collecting the underlying stream.
     * <p>
     * Each {@code int} value in the stream is written as a single byte to a
     * {@link ByteArrayOutputStream}, and the resulting byte array is returned.
     * The stream is consumed by this operation, so the method should be called
     * at most once per instance.
     *
     * @return the collected byte content as an array
     * @throws InvariantViolation if an invariant is violated while collecting
     *         the stream
     */
    @Override
    public byte[] content() throws InvariantViolation {
        return this.src
            .collect(
                ByteArrayOutputStream::new,
                ByteArrayOutputStream::write,
                (l, r) -> {}
            )
            .toByteArray();
    }
}
