package org.ejavdge.web.driver.jdk.socket;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.scalar.num.NumOfText;
import org.ejavdge.scalar.text.Concat;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.web.context.Location;
import org.ejavdge.web.media.Gist;

import java.io.IOException;
import java.net.Socket;

public final class Inet {
    private final Text host;
    private final Num port;

    public Inet(final Location loc) {
        this(
            new Concat(
                new Gist.ImprintOf(
                    new Location.Host(loc)
                )
            ),
            new NumOfText(
                new Concat(
                    new Gist.ImprintOf(
                        new Location.Port(loc)
                    )
                )
            )
        );
    }

    public Inet(final Text h, final Num p) {
        this.host = h;
        this.port = p;
    }

    public Socket socket() throws InvariantViolation {
        try {
            return new Socket(
                this.host.content(),
                this.port.value()
            );
        } catch (final IOException e) {
            throw new InvariantViolation(
                "There is no socket.\n" + e.getMessage()
            );
        }
    }
}
