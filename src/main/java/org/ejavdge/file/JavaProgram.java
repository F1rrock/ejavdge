package org.ejavdge.file;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.bytes.Utf8;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.scalar.text.Utf8Text;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class JavaProgram implements ByteFile, Program {
    private final ByteFile file;
    private final Text path;

    public JavaProgram(final ByteFile f, final Text p) {
        this.file = f;
        this.path = p;
    }

    @Override
    public String name() throws InvariantViolation {
        return this.file.name();
    }

    @Override
    public byte[] content() throws InvariantViolation {
        return this.file.content();
    }

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
