package org.ejavdge.web.driver.jdk.stream;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;

import java.util.stream.IntStream;

import java.io.ByteArrayOutputStream;

/**
 * A {@link Bytes} view over an {@link IntStream} of byte values.
 * <p>
 * This class adapts a stream of {@code int} values — each expected to be in the
 * range of a single byte, {@code 0} to {@code 255} — into a raw byte array. It
 * is commonly used to convert a line of data read from a socket (represented
 * as an {@code IntStream}) back into a {@link Bytes} value for further
 * processing, such as UTF-8 decoding or length computation.
 * <p>
 * The conversion is performed by collecting the stream into a
 * {@link ByteArrayOutputStream}, writing each {@code int} value as a single
 * byte. Because {@code ByteArrayOutputStream.write(int)} writes only the
 * low-order eight bits of its argument, the caller is expected to supply values
 * that already fit in the byte range.
 * <p>
 * An instance can be created either from an existing {@link IntStream} or from
 * a varargs array of {@code int} values.
 */
public final class BytesOfLine implements Bytes {

    /**
     * The underlying stream of byte values.
     */
    private final IntStream src;

    /**
     * Creates a byte sequence from the given values.
     *
     * @param src the byte values, each expected to fit in the range {@code 0}
     *            to {@code 255}
     */
    public BytesOfLine(final int ...src) {
        this.src = IntStream.of(src);
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
