package org.ejavdge.web.driver.jdk.socket;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.scalar.num.NumAbout;
import org.ejavdge.scalar.num.NumOfText;
import org.ejavdge.scalar.text.Concat;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.scalar.text.TextAbout;
import org.ejavdge.web.context.Location;
import org.ejavdge.web.media.Gist;

import java.io.IOException;
import java.net.Socket;

/**
 * A network endpoint consisting of a host and a port, with the ability to open
 * a socket connection to it.
 * <p>
 * This class represents the destination of a TCP connection used by the
 * socket-based web driver. It stores the host name and port as lazily
 * materialized values ({@link Text} and {@link Num} respectively), and provides
 * a {@link #socket()} method that opens a new {@link Socket} to that endpoint.
 * <p>
 * An endpoint can be created directly from a host text and a port number, or
 * derived from a {@link Location} context. In the latter case, the host and
 * port are extracted from the location using a {@link Gist.ImprintOf} with the
 * {@link Location.Host} and {@link Location.Port} contexts respectively. The
 * extracted host is wrapped in {@link TextAbout} with the subject
 * {@code "host"}, and the extracted port text is parsed into a number via
 * {@link NumOfText} and wrapped in {@link NumAbout} with the subject
 * {@code "port"}, so that any failure while materializing these values is
 * reported with a meaningful message.
 */
public final class Inet {

    /**
     * The host name of the endpoint, wrapped for diagnostics.
     */
    private final Text host;

    /**
     * The port number of the endpoint, wrapped for diagnostics.
     */
    private final Num port;

    /**
     * Creates an endpoint from the given {@link Location} context.
     * <p>
     * The host and port are extracted from the location using the
     * {@link Location.Host} and {@link Location.Port} contexts, imprinted via
     * {@link Gist.ImprintOf}. The resulting host text is wrapped in
     * {@link TextAbout} with the subject {@code "host"}, and the resulting port
     * text is parsed into a number via {@link NumOfText} and wrapped in
     * {@link NumAbout} with the subject {@code "port"}.
     *
     * @param loc the location from which the host and port are extracted
     */
    public Inet(final Location loc) {
        this(
            new TextAbout(
                "host",
                new Concat(
                    new Gist.ImprintOf(
                        new Location.Host(loc)
                    )
                )
            ),
            new NumAbout(
                "port",
                new NumOfText(
                    new Concat(
                        new Gist.ImprintOf(
                            new Location.Port(loc)
                        )
                    )
                )
            )
        );
    }

    /**
     * Creates an endpoint from the given host and port.
     *
     * @param h the host name of the endpoint
     * @param p the port number of the endpoint
     */
    public Inet(final Text h, final Num p) {
        this.host = h;
        this.port = p;
    }

    /**
     * Opens a new socket connection to this endpoint.
     * <p>
     * The host and port values are materialized via {@link Text#content()} and
     * {@link Num#value()} respectively, and a new {@link Socket} is created to
     * that address. If the connection cannot be established due to an I/O
     * error, an {@link InvariantViolation} is thrown with the message
     * {@code "There is no socket.\n"} and the underlying {@link IOException} as
     * the cause.
     *
     * @return a newly opened socket connected to the endpoint
     * @throws InvariantViolation if the host or port cannot be materialized, or
     *         if the socket cannot be created due to an I/O error
     */
    public Socket socket() throws InvariantViolation {
        try {
            return new Socket(
                this.host.content(),
                this.port.value()
            );
        } catch (final IOException e) {
            throw new InvariantViolation(
                "There is no socket.\n",
                e
            );
        }
    }
}
