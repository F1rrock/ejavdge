package org.ejavdge.web.driver.jdk.socket;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.scalar.num.Positive;
import org.ejavdge.web.context.Location;
import org.ejavdge.web.driver.WebDriver;
import org.ejavdge.web.spec.Request;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public final class JdkSocket implements WebDriver {
    private final Positive bufSize;

    public JdkSocket() {
        this(4096);
    }

    public JdkSocket(final int bufSize) {
        this(new Num.Of(bufSize));
    }

    public JdkSocket(final Num bufSize) {
        this.bufSize = new Positive(bufSize);
    }

    @Override
    public byte[] resourceOf(final Location loc, final Request req) {
        try {
            try (final var socket = new Inet(loc).socket()) {
                final OutputStream out = socket.getOutputStream();
                out.write(req.bytes());
                out.flush();
                try (final var in = socket.getInputStream()) {
                    final var responseBuffer = new ByteArrayOutputStream();
                    final byte[] buffer = new byte[this.bufSize.value()];

                    int bytesRead;
                    while ((bytesRead = in.read(buffer)) != -1) {
                        responseBuffer.write(buffer, 0, bytesRead);
                    }

                    return responseBuffer.toByteArray();
                }
            }
        } catch (final IOException e) {
            throw new InvariantViolation(
                "There is no valid resource.\n" + e.getMessage()
            );
        }
    }
}
