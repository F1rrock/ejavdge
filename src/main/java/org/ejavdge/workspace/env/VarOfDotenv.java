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

public final class VarOfDotenv implements EnvVariable {
    private final Text name;
    private final Text directory;
    private final Text filename;

    public VarOfDotenv(final String s) {
        this(new Text.Of(s));
    }

    public VarOfDotenv(final Text t) {
        this(t, new Text.Of("./"), new Text.Of(".env"));
    }

    public VarOfDotenv(final String n, final String d, final String f) {
        this(new Text.Of(n), new Text.Of(d), new Text.Of(f));
    }

    public VarOfDotenv(final Text n, final Text d, final Text f) {
        this.name = n;
        this.directory = d;
        this.filename = f;
    }

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
