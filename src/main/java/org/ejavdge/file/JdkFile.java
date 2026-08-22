package org.ejavdge.file;

import org.ejavdge.error.InvariantViolation;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public final class JdkFile implements ByteFile {
    private final File src;

    public JdkFile(final File f) {
        this.src = f;
    }

    @Override
    public String name() throws InvariantViolation {
        return this.src.getName();
    }

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
