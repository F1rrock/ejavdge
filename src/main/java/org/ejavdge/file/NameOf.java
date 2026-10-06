package org.ejavdge.file;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

/**
 * An adapter that exposes the name of a {@link ByteFile} as a {@link Text}.
 * <p>
 * This class implements {@link Text} and wraps a {@link ByteFile}, delegating
 * the retrieval of textual content to the file's {@link ByteFile#name()}
 * method. It allows the name of a file to be used wherever a {@code Text} is
 * expected, such as in string concatenation, formatting, or further processing.
 * <p>
 * This is useful when only the file name is needed as text, without requiring
 * access to the file's binary content.
 */
public final class NameOf implements Text {

    /**
     * The underlying byte file whose name is exposed as text.
     */
    private final ByteFile src;

    /**
     * Creates a new text view over the name of the given byte file.
     *
     * @param f the byte file whose name will be used as the text content
     */
    public NameOf(final ByteFile f) {
        this.src = f;
    }

    /**
     * Returns the name of the underlying file.
     * <p>
     * This method delegates to the file's {@link ByteFile#name()} method.
     *
     * @return the file name as a string
     * @throws InvariantViolation if an invariant is violated while retrieving
     *         the name
     */
    @Override
    public String content() throws InvariantViolation {
        return this.src.name();
    }
}
