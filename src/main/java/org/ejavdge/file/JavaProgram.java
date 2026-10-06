package org.ejavdge.file;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.bytes.Utf8;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.scalar.text.Utf8Text;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * A Java program that can be treated both as a byte file and as an executable
 * program.
 * <p>
 * This class implements {@link ByteFile} and {@link Program}, combining the
 * ability to expose the source code as named bytes with the ability to execute
 * the program against a given input and capture its output.
 * <p>
 * Execution works by writing the source code to a temporary {@code .java}
 * file, launching it with the {@code java} command in single-file source mode,
 * feeding the given input to its standard input, and reading everything it
 * writes to standard output and standard error (which are merged). The
 * temporary file is deleted after execution, and the process is destroyed if it
 * is still running when the operation completes.
 * <p>
 * If the process exits with a non-zero status code, an
 * {@link InvariantViolation} is thrown that includes the exit code and the
 * captured output. If the program cannot be started or the temporary file
 * cannot be created or written, an {@link InvariantViolation} wrapping the
 * underlying {@link IOException} (or {@link InterruptedException}) is thrown.
 */
public final class JavaProgram implements ByteFile, Program {

    /**
     * The underlying file that provides the source code and its name.
     */
    private final ByteFile file;

    /**
     * The working directory in which the program is executed.
     */
    private final Text path;

    /**
     * Creates a Java program from the given source file and working directory.
     *
     * @param f the byte file containing the Java source code
     * @param p the working directory in which the program will be run
     */
    public JavaProgram(final ByteFile f, final Text p) {
        this.file = f;
        this.path = p;
    }

    /**
     * Returns the name of this Java program.
     * <p>
     * This method delegates to the underlying file's
     * {@link ByteFile#name()} method.
     *
     * @return the name of the program
     * @throws InvariantViolation if an invariant is violated while retrieving
     *         the name
     */
    @Override
    public String name() throws InvariantViolation {
        return this.file.name();
    }

    /**
     * Returns the raw source code of this Java program.
     * <p>
     * This method delegates to the underlying file's
     * {@link ByteFile#content()} method.
     *
     * @return the source code as a byte array
     * @throws InvariantViolation if an invariant is violated while retrieving
     *         the content
     */
    @Override
    public byte[] content() throws InvariantViolation {
        return this.file.content();
    }

    /**
     * Runs the Java program with the given input and returns its output.
     * <p>
     * The source code is written to a temporary {@code .java} file, then
     * executed with the {@code java} command in single-file source mode, using
     * the configured working directory. The provided input is fed to the
     * process's standard input, and the process's standard output and standard
     * error streams are merged and captured.
     * <p>
     * If the process exits with a non-zero status code, an
     * {@link InvariantViolation} is thrown that includes the exit code and the
     * captured output. If an I/O or interruption error occurs while creating,
     * writing, or running the program, an {@link InvariantViolation} wrapping
     * the underlying cause is thrown and the thread's interrupt flag is
     * restored.
     * <p>
     * In all cases, the temporary source file is deleted and, if the child
     * process is still alive, it is destroyed.
     *
     * @param input the input data to feed to the program's standard input
     * @return the program's combined standard output and standard error
     * @throws InvariantViolation if the program exits with a non-zero status
     *         code, or if it cannot be created, written, started, or waited
     *         for
     */
    @Override
    public String outcomeOf(final Text input) throws InvariantViolation {
        try {
            final var src = Files.createTempFile("ejavdge", ".java");
            try {
                Files.write(src, this.file.content());
                final var p = new ProcessBuilder("java", src.toString())
                    .directory(Path.of(this.path.content()).toFile())
                    .redirectErrorStream(true)
                    .start();
                try {
                    p.getOutputStream().write(new Utf8(input).content());
                    p.getOutputStream().close();
                    final var out = new Utf8Text(
                        new Bytes.Of(p.getInputStream().readAllBytes())
                    ).content();
                    final var code = p.waitFor();
                    if (code != 0) {
                        throw new InvariantViolation(
                            "There is no outcome because program exited with "
                                + code + ":\n" + out
                        );
                    }
                    return out;
                } finally {
                    if (p.isAlive()) {
                        p.destroy();
                    }
                }
            } finally {
                Files.deleteIfExists(src);
            }
        } catch (final IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new InvariantViolation(
                "There is no outcome because program cannot run",
                e
            );
        }
    }
}
