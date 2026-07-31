package org.ejavdge.web.driver.jdk.socket;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.scalar.num.Positive;
import org.ejavdge.web.context.Location;
import org.ejavdge.web.driver.WebDriver;
import org.ejavdge.web.spec.Request;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

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
            try (var socket = new Inet(loc).socket()) {
                var out = socket.getOutputStream();
                out.write(req.bytes());
                out.flush();
                try (var in = socket.getInputStream()) {
                    final var response = new ByteArrayOutputStream();
                    final byte[] buffer = new byte[this.bufSize.value()];
                    int bytesRead;
                    while ((bytesRead = in.read(buffer)) != -1) {
                        response.write(buffer, 0, bytesRead);
                    }
                    return response.toByteArray();
                }
            }
        } catch (final IOException e) {
            throw new InvariantViolation(
                "There is no valid resource.\n" + e.getMessage()
            );
        }
    }
}
