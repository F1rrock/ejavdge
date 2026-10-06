package org.ejavdge.workspace.env;

import io.github.cdimascio.dotenv.DotenvBuilder;
import io.github.cdimascio.dotenv.DotenvException;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.BindOfText;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.scalar.text.TextAbout;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * An {@link EnvVariable} implementation backed by a {@code .env} file.
 * <p>
 * This class reads and writes variables from a dotenv file — a simple
 * {@code KEY=VALUE} text file commonly used to configure applications. The
 * variable's name, the directory containing the file, and the file name are all
 * configurable, defaulting to a variable name supplied by the caller, the
 * current working directory ({@code "./"}), and the file name {@code ".env"}.
 * <p>
 * Reading a variable is performed using the {@code dotenv-java} library: the
 * file is loaded from the configured directory and file name, and the value of
 * the requested key is returned. If the key is not present, or if the file is
 * malformed, an {@link InvariantViolation} is thrown with a descriptive
 * message.
 * <p>
 * Writing a variable is performed by rewriting the file: any existing line that
 * defines the same key is removed, and a new line of the form
 * {@code KEY=VALUE} prepended to the remaining contents. If the file does
 * not exist, an {@link InvariantViolation} is thrown rather than creating a new
 * one, so as not to silently discard an intended configuration.
 */
public final class VarOfDotenv implements EnvVariable {

    /**
     * The name of the environment variable.
     */
    private final Text name;

    /**
     * The directory containing the dotenv file.
     */
    private final Text directory;

    /**
     * The file name of the dotenv file.
     */
    private final Text filename;

    /**
     * Creates a dotenv variable with the given name, using the default
     * directory {@code "./"} and the default file name {@code ".env"}.
     *
     * @param s the name of the environment variable
     */
    public VarOfDotenv(final String s) {
        this(new Text.Of(s));
    }

    /**
     * Creates a dotenv variable with the given name, using the default
     * directory {@code "./"} and the default file name {@code ".env"}.
     *
     * @param t the name of the environment variable
     */
    public VarOfDotenv(final Text t) {
        this(t, new Text.Of("./"), new Text.Of(".env"));
    }

    /**
     * Creates a dotenv variable with the given name, directory, and file name.
     *
     * @param n the name of the environment variable
     * @param d the directory containing the dotenv file
     * @param f the file name of the dotenv file
     */
    public VarOfDotenv(final String n, final String d, final String f) {
        this(new Text.Of(n), new Text.Of(d), new Text.Of(f));
    }

    /**
     * Creates a dotenv variable with the given name, directory, and file name.
     *
     * @param n the name of the environment variable
     * @param d the directory containing the dotenv file
     * @param f the file name of the dotenv file
     */
    public VarOfDotenv(final Text n, final Text d, final Text f) {
        this.name = n;
        this.directory = d;
        this.filename = f;
    }

    /**
     * Returns the current value of this dotenv variable.
     * <p>
     * The variable name is materialized, and the dotenv file is loaded from the
     * configured directory and file name using the {@code dotenv-java} library.
     * The value associated with the variable name is then returned.
     * <p>
     * The result is wrapped in a {@link TextAbout} with the subject
     * {@code "dotenv variable <name>"}, so any failure reported with a
     * meaningful message:
     * <ul>
     *   <li>if the variable is not defined in the file, an
     *       {@link InvariantViolation} with the message
     *       {@code "There is no dotenv variable <name>"} is thrown;</li>
     *   <li>if the file cannot be parsed, an {@link InvariantViolation} with
     *       the message {@code "there is broken .env"} is thrown, wrapping the
     *       underlying {@link DotenvException}.</li>
     * </ul>
     *
     * @return the current value of the environment variable
     * @throws InvariantViolation if the variable is not defined, the dotenv
     *         file is malformed, or the variable name cannot be materialized
     */
    @Override
    public String value() throws InvariantViolation {
        return new BindOfText(
            this.name,
            n -> new TextAbout(
                "dotenv variable %s".formatted(n),
                () -> {
                    try {
                        final var v = new DotenvBuilder()
                            .directory(this.directory.content())
                            .filename(this.filename.content()).load().get(n);
                        if (v == null) {
                            throw new InvariantViolation(
                                "There is no dotenv variable " + n
                            );
                        }
                        return v;
                    } catch (final DotenvException e) {
                        throw new InvariantViolation(
                            "there is broken .env",
                            e
                        );
                    }
                }
            )
        ).content();
    }

    /**
     * Assigns a new value to this dotenv variable by rewriting the dotenv file.
     * <p>
     * The variable name, directory, and file name are materialized, and the
     * target path is resolved. If the file does not exist, an
     * {@link InvariantViolation} with the message
     * {@code "There is no .env file"} is thrown rather than creating a new one.
     * <p>
     * Otherwise, the file is rewritten: a new line of the form
     * {@code KEY=VALUE} prepended, and any existing line that defines the
     * same key removed. Other lines preserved unchanged.
     * <p>
     * If an I/O error occurs while reading or writing the file, an
     * {@link InvariantViolation} with the message
     * {@code ".env file has not overwritten"} is thrown, wrapping the
     * underlying {@link IOException}.
     *
     * @param v the new value to assign to the variable
     * @throws InvariantViolation if the dotenv file does not exist, cannot be
     *         read or written, or the variable name cannot be materialized
     */
    @Override
    public void assignWith(final Text v) throws InvariantViolation {
        final var key = this.name.content();
        final var path = Path.of(
            this.directory.content(),
            this.filename.content()
        );
        if (!Files.exists(path)) {
            throw new InvariantViolation("There is no .env file");
        }
        final var pattern = Pattern.compile("^" + Pattern.quote(key) + "=");
        try {
            Files.write(
                path,
                Stream.concat(
                    Stream.of("%s=%s".formatted(key, v.content())),
                    Files.readAllLines(path)
                        .stream()
                        .filter(line -> !pattern.matcher(line).find())
                ).toList()
            );
        } catch (final IOException e) {
            throw new InvariantViolation(
                ".env file has not overwritten",
                e
            );
        }
    }
}
