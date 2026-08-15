package org.ejavdge.web.driver.jdk.socket;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.web.driver.jdk.stream.ByteStream;

import java.io.IOException;
import java.util.stream.IntStream;

public final class BytesOfReply implements ByteStream {
    private final Reply rep;

    public BytesOfReply(final Reply r) {
        this.rep = r;
    }

    public IntStream content() throws InvariantViolation {
        final var stream = this.rep.stream();
        return IntStream.generate(() -> {
            try {
                return stream.read();
            } catch (final IOException e) {
                throw new InvariantViolation(
                    "There is no bytes to read.\n" + e.getMessage()
                );
            }
        }).onClose(() -> {
            try {
                stream.close();
            } catch (final IOException e) {
                throw new InvariantViolation(
                    "There is no socket to close.\n" + e.getMessage()
                );
            }
        });
    }
}
