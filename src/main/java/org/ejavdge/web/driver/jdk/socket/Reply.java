package org.ejavdge.web.driver.jdk.socket;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.web.context.Location;
import org.ejavdge.web.spec.Request;

import java.io.IOException;
import java.io.InputStream;

public final class Reply {
    private final Location loc;
    private final Request req;

    public Reply(final Location l, final Request r) {
        this.loc = l;
        this.req = r;
    }

    public InputStream stream() throws InvariantViolation {
        var socket = new Inet(this.loc).socket();
        try {
            var out = socket.getOutputStream();
            out.write(this.req.bytes());
            out.flush();
            return socket.getInputStream();
        } catch (final IOException e) {
            try {
                socket.close();
            } catch (final IOException ex) {
                e.addSuppressed(ex);
                throw new InvariantViolation(
                    "There is no socket to close.\n" + e.getMessage()
                );
            }
            throw new InvariantViolation(
                "There is no valid resource.\n" + e.getMessage()
            );
        }
    }
}
