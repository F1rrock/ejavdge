package org.ejavdge.web.driver.jdk.socket;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.web.context.Location;
import org.ejavdge.web.spec.Request;

import java.io.IOException;
import java.io.InputStream;

/**
 * A single request-reply cycle performed over a TCP socket.
 * <p>
 * This class encapsulates the low-level interaction with a remote HTTP server
 * using a plain {@link java.net.Socket}. It is responsible for:
 * <ul>
 *   <li>opening a socket connection to the target host and port, as determined
 *       by the given {@link Location};</li>
 *   <li>writing the raw bytes of the given {@link Request} to the socket's
 *       output stream;</li>
 *   <li>returning the socket's input stream so that the response can be read
 *       by the caller.</li>
 * </ul>
 * <p>
 * The socket is opened via {@link Inet#socket()}, which resolves the host and
 * port from the location. If any I/O error occurs while writing the request or
 * obtaining the input stream, the socket is closed and an
 * {@link InvariantViolation} is thrown with an appropriate message.
 * <p>
 * Note that the returned {@link InputStream} is backed by the socket. The
 * caller is responsible for consuming it and, eventually, closing it, which
 * will also close the underlying socket. The {@link BytesOfReply} adapter is
 * typically used to expose this stream as a stream of byte values for further
 * processing.
 */
public final class Reply {

    /**
     * The location of the target resource, used to determine the host and port
     * for the socket connection.
     */
    private final Location loc;

    /**
     * The request to be sent over the socket.
     */
    private final Request req;

    /**
     * Creates a new request-reply cycle for the given location and request.
     *
     * @param l the location of the target resource
     * @param r the request to send
     */
    public Reply(final Location l, final Request r) {
        this.loc = l;
        this.req = r;
    }

    /**
     * Opens a socket connection, sends the request, and returns the input
     * stream for reading the response.
     * <p>
     * The method first obtains a new socket via {@link Inet#socket()}, using
     * the location's host and port. It then writes the raw bytes of the request
     * to the socket's output stream and flushes it, ensuring that the request
     * is fully sent. Finally, it returns the socket's input stream, which the
     * caller can use to read the response.
     * <p>
     * If an {@link IOException} occurs at any point, the method attempts to
     * close the socket. If closing also fails, the original exception is
     * augmented with the closing exception as a suppressed exception, and an
     * {@link InvariantViolation} with the message
     * {@code "There is no socket to close.\n"} is thrown. Otherwise, an
     * {@link InvariantViolation} with the message
     * {@code "There is no valid resource.\n"} is thrown, wrapping the original
     * I/O error.
     *
     * @return an input stream for reading the response from the socket
     * @throws InvariantViolation if the socket cannot be opened, the request
     *         cannot be sent, or the input stream cannot be obtained
     */
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
                    "There is no socket to close.\n",
                    e
                );
            }
            throw new InvariantViolation(
                "There is no valid resource.\n",
                e
            );
        }
    }
}
