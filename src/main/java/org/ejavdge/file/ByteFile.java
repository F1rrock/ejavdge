package org.ejavdge.file;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.text.Text;

/**
 * A contract for a named file whose contents are raw bytes.
 * <p>
 * A {@code ByteFile} represents a file with a name and binary content. It is
 * used throughout the application to model files such as solution sources or
 * problem attachments, where both the file name (for identification or
 * submission) and the raw bytes (for reading or uploading) are needed.
 * <p>
 * Implementations expose the file name as a {@link String} and the file
 * content as a {@code byte[]}. Both accessors may throw an
 * {@link InvariantViolation} if the underlying data cannot be obtained.
 */
public interface ByteFile {

    /**
     * Returns the name of this file.
     *
     * @return the file name as a string
     * @throws InvariantViolation if an invariant is violated while retrieving
     *         the name
     */
    String name() throws InvariantViolation;

    /**
     * Returns the raw byte content of this file.
     *
     * @return the file content as a byte array
     * @throws InvariantViolation if an invariant is violated while retrieving
     *         the content
     */
    byte[] content() throws InvariantViolation;

    /**
     * A simple {@link ByteFile} implementation that wraps a {@link Text} for
     * the file name and a {@link Bytes} for the file content.
     */
    final class Of implements ByteFile {

        /**
         * The name of the file.
         */
        private final Text name;

        /**
         * The raw byte content of the file.
         */
        private final Bytes value;

        /**
         * Creates a new file from the given name and content.
         *
         * @param n  the file name
         * @param bs the file content as bytes
         */
        public Of(final Text n, final Bytes bs) {
            this.name = n;
            this.value = bs;
        }

        /**
         * Returns the name of this file.
         *
         * @return the file name as a string
         * @throws InvariantViolation if an invariant is violated while
         *         retrieving the name
         */
        @Override
        public String name() throws InvariantViolation {
            return this.name.content();
        }

        /**
         * Returns the raw byte content of this file.
         *
         * @return the file content as a byte array
         * @throws InvariantViolation if an invariant is violated while
         *         retrieving the content
         */
        @Override
        public byte[] content() throws InvariantViolation {
            return this.value.content();
        }
    }
}
