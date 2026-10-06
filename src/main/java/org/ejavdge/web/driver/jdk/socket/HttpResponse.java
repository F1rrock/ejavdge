package org.ejavdge.web.driver.jdk.socket;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.bytes.Memo;
import org.ejavdge.web.driver.jdk.stream.*;

import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * An HTTP response parsed from a raw socket byte stream.
 * <p>
 * This class represents a complete HTTP response as received over a socket,
 * separating it into two logical parts: the header block and the body. The
 * parsing is performed lazily and in a streaming fashion, so the full response
 * does not need to be buffered in memory before it can be used.
 * <p>
 * The response is parsed using a {@link Tee}, which duplicates the incoming
 * byte stream into two independent branches:
 * <ul>
 *   <li>the <b>left</b> branch is used as the body stream, starting from the
 *       very beginning of the response;</li>
 *   <li>the <b>right</b> branch is used to extract the headers, by splitting
 *       the stream into lines and taking everything up to (but not including)
 *       the first empty line.</li>
 * </ul>
 * The header block is memoized via {@link Memo}, so repeated calls to
 * {@link #headers()} do not reparse the stream. The body stream exposed by
 * {@link #body()} skips past the header block (plus the two bytes of the
 * terminating blank line) so that consumers read only the actual response body.
 * <p>
 * The line separators used to split the header block are configurable. By
 * default, the standard HTTP line ending {@code CRLF} ({@code '\r'} followed by
 * {@code '\n'}) is assumed.
 */
public final class HttpResponse {

    /**
     * The stream of bytes representing the body of the response, starting from
     * the beginning of the response.
     */
    private final IntStream stream;

    /**
     * The memoized header block of the response.
     */
    private final Bytes headers;

    /**
     * Creates an HTTP response from the given raw byte stream, assuming standard
     * {@code CRLF} line endings.
     * <p>
     * The byte stream is duplicated via a {@link Tee}, and the resulting
     * branches are passed to the more general constructor with {@code '\r'} and
     * {@code '\n'} as the line separator characters.
     *
     * @param bs the raw byte stream of the HTTP response
     */
    public HttpResponse(final ByteStream bs) {
        this(new Tee<>(bs.content().boxed()), '\r', '\n');
    }

    /**
     * Creates an HTTP response from the given duplicated byte stream, using the
     * specified line separator characters.
     * <p>
     * The left branch of the tee is retained as the body stream (starting from
     * the beginning of the response), and the right branch is used to parse the
     * header block. Headers are extracted by splitting the stream into lines
     * using {@code lf} as the separator and taking lines until an empty line
     * (consisting solely of {@code cr}) is encountered. The extracted header
     * bytes are memoized so that the parsing is performed at most once.
     *
     * @param tee the duplicated byte stream of the HTTP response
     * @param cr  the carriage-return character used to detect the end of the
     *            header block
     * @param lf  the line-feed character used to split the stream into lines
     */
    public HttpResponse(final Tee<Integer> tee, final char cr, final char lf) {
        this.stream = tee.left().mapToInt(Integer::intValue);
        this.headers = new Memo(
            new BytesOfStream(
                new ConcatOfLines(
                    lf,
                    new Lines(
                        new ByteStream.Of(
                            tee.right().mapToInt(Integer::intValue)
                        ),
                        lf
                    ).content().takeWhile(
                        ln -> !Arrays.equals(ln, new int[] {cr})
                    )
                ).content()
            )
        );
    }

    /**
     * Returns the raw header block of the response as a byte array.
     * <p>
     * The header block includes the status line and all response headers, as
     * received, up to (but not including) the terminating blank line. The result
     * is memoized: the first invocation parses the headers from the underlying
     * stream, and subsequent invocations return the cached bytes.
     *
     * @return the raw HTTP headers as a byte array
     * @throws InvariantViolation if an invariant is violated while retrieving or
     *         parsing the headers
     */
    public byte[] headers() throws InvariantViolation {
        return this.headers.content();
    }

    /**
     * Returns the body of the response as a stream of bytes.
     * <p>
     * The returned stream starts immediately after the header block and its
     * terminating blank line. Specifically, it skips the number of bytes equal
     * to the header length plus two (for the blank line separator). The stream
     * is not buffered; consumers read the body lazily from the underlying
     * socket.
     * <p>
     * Note that the body stream is shared with the underlying response stream,
     * so it should be consumed only once. If the response uses a bounded body
     * encoding (such as {@code Content-Length} or chunked transfer encoding),
     * the appropriate decoding policy should be applied to the returned stream
     * to read the correct number of bytes.
     *
     * @return the body of the response as a stream of byte values
     * @throws InvariantViolation if an invariant is violated while determining
     *         the header length
     */
    public IntStream body() throws InvariantViolation {
        return this.stream.skip(this.headers.content().length + 2L);
    }
}
