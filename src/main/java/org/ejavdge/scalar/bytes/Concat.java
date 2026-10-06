package org.ejavdge.scalar.bytes;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.items.Items;

import java.io.ByteArrayOutputStream;

/**
 * A {@link Bytes} implementation that concatenates the contents of several byte
 * sequences into a single contiguous byte array.
 * <p>
 * This class represents the concatenation of multiple {@link Bytes} instances.
 * When {@link #content()} is called, each constituent byte sequence is
 * materialized, and their contents are appended together in the order they were
 * provided. The result is a single byte array containing all the bytes of the
 * inputs, end to end.
 * <p>
 * The byte sequences can be passed either as varargs of {@link Bytes}, or as an
 * {@code Items} of bytes (using a wildcard type to allow any {@code Bytes}
 * subtype). This makes {@code Concat} useful for assembling a payload from
 * multiple sources, such as combining a text header with a binary file body
 * before sending it in a single request.
 */
public final class Concat implements Bytes {

    /**
     * The collection of byte sequences to be concatenated.
     */
    private final Items<? extends Bytes> bss;

    /**
     * Creates a concatenation of the given byte sequences.
     *
     * @param b the byte sequences to concatenate, in order
     */
    public Concat(final Bytes ...b) {
        this(new Items.Of<>(b));
    }

    /**
     * Creates a concatenation of the given collection of byte sequences.
     *
     * @param b the collection of byte sequences to concatenate, in order
     */
    public Concat(final Items<? extends Bytes> b) {
        this.bss = b;
    }

    /**
     * Returns the concatenated contents of all constituent byte sequences.
     * <p>
     * Each byte sequence is materialized in the order it was provided, and its
     * bytes are appended to a single growing buffer. If any constituent
     * sequence fails to produce its content, the corresponding
     * {@link InvariantViolation} is propagated.
     *
     * @return a single byte array containing all bytes of the inputs, in order
     * @throws InvariantViolation if any constituent byte sequence violates an
     *         invariant while materializing its contents
     */
    @Override
    public byte[] content() throws InvariantViolation {
        return this.bss.contents()
            .stream()
            .map(Bytes::content)
            .collect(
                ByteArrayOutputStream::new,
                ByteArrayOutputStream::writeBytes,
                (l, r) -> l.writeBytes(r.toByteArray())
            )
            .toByteArray();
    }
}
