package org.ejavdge.file;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;

/**
 * An adapter that exposes the content of a {@link ByteFile} as {@link Bytes}.
 * <p>
 * This class allows the raw content of a byte file to be used wherever a
 * {@link Bytes} value is expected, without regard to the file's name. The
 * {@link #content()} method simply delegates to the underlying file's
 * {@link ByteFile#content()} method.
 * <p>
 * This is useful when only the binary payload of a file is relevant, such as
 * when decoding text, computing a hash, or passing the content to a lower-level
 * API that operates on raw bytes.
 */
public final class ContentOf implements Bytes {

    /**
     * The underlying byte file whose content is exposed.
     */
    private final ByteFile src;

    /**
     * Creates a new bytes view over the given byte file.
     *
     * @param f the file whose content will be exposed as bytes
     */
    public ContentOf(final ByteFile f) {
        this.src = f;
    }

    /**
     * Returns the raw content of the underlying file.
     *
     * @return the file content as a byte array
     * @throws InvariantViolation if an invariant is violated while retrieving
     *         the content
     */
    @Override
    public byte[] content() throws InvariantViolation {
        return this.src.content();
    }
}
