package org.ejavdge.file;

import org.ejavdge.error.InvariantViolation;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * A {@link ByteFile} implementation backed by a {@link java.io.File} from the
 * JDK.
 * <p>
 * This class adapts a standard Java {@code File} to the {@code ByteFile}
 * interface, exposing its name via {@link #name()} and its raw contents via
 * {@link #content()}. The content is read from disk on demand using
 * {@link Files#readAllBytes(java.nio.file.Path)}.
 * <p>
 * This is useful for integrating ordinary files on the local file system with
 * the rest of the application, which operates on the {@code ByteFile}
 * abstraction.
 */
public final class JdkFile implements ByteFile {

    /**
     * The underlying JDK file.
     */
    private final File src;

    /**
     * Creates a byte file backed by the given JDK file.
     *
     * @param f the underlying {@link File}
     */
    public JdkFile(final File f) {
        this.src = f;
    }

    /**
     * Returns the name of the underlying file.
     * <p>
     * This is the file name without its parent directory, as returned by
     * {@link File#getName()}.
     *
     * @return the file name
     * @throws InvariantViolation if an invariant is violated while retrieving
     *         the name
     */
    @Override
    public String name() throws InvariantViolation {
        return this.src.getName();
    }

    /**
     * Returns the raw contents of the underlying file.
     * <p>
     * The entire file is read into memory as a byte array. If the file cannot
     * be read due to an I/O error, an {@link InvariantViolation} is thrown
     * wrapping the underlying {@link IOException}.
     *
     * @return the file contents as a byte array
     * @throws InvariantViolation if the file cannot be read
     */
    @Override
    public byte[] content() throws InvariantViolation {
        try {
            return Files.readAllBytes(this.src.toPath());
        } catch (final IOException e) {
            throw new InvariantViolation(
                "there is no valid content", e
            );
        }
    }
}
