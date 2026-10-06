package org.ejavdge.web.driver.jdk.socket;

import org.ejavdge.scalar.bytes.*;
import org.ejavdge.web.context.Location;
import org.ejavdge.web.driver.WebDriver;
import org.ejavdge.web.driver.jdk.socket.body.*;
import org.ejavdge.web.spec.Request;
import org.ejavdge.web.spec.Terminator;

/**
 * A {@link WebDriver} implementation that performs HTTP requests using plain
 * JDK sockets.
 * <p>
 * This driver is the lowest-level HTTP client in the application. Instead of
 * relying on a higher-level HTTP library, it opens a TCP connection to the
 * target host and port, sends the raw request bytes, and reads the response
 * directly from the socket. This gives the driver full control over the wire
 * format, which is useful for interacting with contest systems that may not be
 * fully compliant with modern HTTP conventions.
 * <p>
 * The processing of a request-response cycle proceeds as follows:
 * <ol>
 *   <li>A {@link Reply} is constructed from the {@link Location} and
 *       {@link Request}, which handles opening the socket, sending the request
 *       bytes, and providing the raw response stream.</li>
 *   <li>The raw response stream is parsed into an {@link HttpResponse}, which
 *       separates the header block from the body.</li>
 *   <li>The header block is memoized via {@link Memo} and wrapped with a
 *       descriptive label using {@link BytesAbout}.</li>
 *   <li>A body decoding policy is chosen based on the response headers via
 *       {@link BodyPolicy}, and the body is read accordingly using
 *       {@link BodyOf}. This handles both {@code Content-Length}-bounded and
 *       chunked transfer-encoded bodies.</li>
 *   <li>The headers, a {@link Terminator} separator, and the decoded body are
 *       concatenated into a single byte array, which is returned to the
 *       caller.</li>
 * </ol>
 * <p>
 * The returned byte array contains the full response as a single payload: the
 * raw header block, a terminator that separates it from the body, and the
 * decoded body bytes. This allows downstream code to work with the response as
 * a single {@link Bytes} value while still being able to distinguish the
 * headers from the body via the terminator.
 * <p>
 * The driver also carries a short descriptive label, used by
 * {@link BytesAbout} to enrich error messages produced while reading the
 * response. The label can be customized via the second constructor, which is
 * useful when several drivers coexist in the same application and their
 * failures need to be told apart in logs.
 */
public final class JdkSocket implements WebDriver {

    /**
     * A short descriptive label attached to errors produced by this driver.
     */
    private final String about;

    /**
     * Creates a new socket-based web driver with the default descriptive
     * label {@code "response of jdk socket web driver"}.
     * <p>
     * This is equivalent to calling
     * {@link #JdkSocket(String) JdkSocket("response of jdk socket web driver")}.
     */
    public JdkSocket() {
        this("response of jdk socket web driver");
    }

    /**
     * Creates a new socket-based web driver with the given descriptive
     * label.
     * <p>
     * The label is used by {@link BytesAbout} when wrapping the response,
     * so that any failure encountered while reading or decoding the
     * response is reported with a message that identifies this driver. The
     * driver itself remains stateless: each call to
     * {@link #resourceOf(Location, Request)} opens a fresh socket
     * connection, sends the request, and reads the response, so a single
     * instance can be reused freely across threads and requests.
     *
     * @param s the descriptive label to attach to errors produced by this
     *          driver
     */
    public JdkSocket(final String s) {
        this.about = s;
    }

    /**
     * Performs the given HTTP request to the specified location and returns the
     * full response as a byte array.
     * <p>
     * The request is sent over a JDK socket, and the response is parsed to
     * separate the headers from the body. The body is decoded according to its
     * transfer encoding (either {@code Content-Length} or chunked). The result
     * is a single byte array containing the header block, a {@link Terminator}
     * separator, and the decoded body.
     * <p>
     * The header block is memoized and labelled as {@code "headers"} for
     * diagnostic purposes, and the decoded body is labelled as {@code "body"}.
     * The body decoding policy selected at runtime based on the header
     * contents. The whole response is additionally labelled with this
     * driver's descriptive label (see {@link #JdkSocket(String)}).
     *
     * @param loc the location of the target resource in the contest system
     * @param req the request to send, including method, headers, and body
     * @return the full response as a byte array, with headers and body separated
     *         by a terminator
     * @throws org.ejavdge.error.InvariantViolation if the request cannot be
     *         sent, the response cannot be read, or the body cannot be decoded
     */
    @Override
    public byte[] resourceOf(final Location loc, final Request req) {
        final var response = new HttpResponse(
            new BytesOfReply(new Reply(loc, req))
        );
        final var headers = new BytesAbout(
            "headers",
            new Memo(new HeadersOf(response))
        );
        return new BytesAbout(
            this.about,
            new Concat(
                headers,
                new Terminator(),
                new BytesAbout(
                    "body",
                    new BindOfBytes(
                        headers,
                        bs -> new BodyOf(
                            response,
                            new BodyPolicy(bs)
                        )
                    )
                )
            )
        ).content();
    }
}
