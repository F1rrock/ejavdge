package org.ejavdge.web.driver.jdk.socket.body;

import java.util.function.UnaryOperator;
import java.util.stream.IntStream;

/**
 * A policy for decoding HTTP response bodies, composed of length and chunked
 * transfer decoding policies.
 * <p>
 * HTTP responses can encode their bodies in several ways. The two most common
 * mechanisms are:
 * <ul>
 *   <li><b>Content-Length</b> – the body length is specified in a header, and
 *       exactly that many bytes follow;</li>
 *   <li><b>Transfer-Encoding: chunked</b> – the body is sent as a sequence of
 *       chunks, each prefixed by its length in hexadecimal, terminated by a
 *       zero-length chunk.</li>
 * </ul>
 * This class implements a {@link UnaryOperator UnaryOperator&lt;IntStream&gt;}
 * that applies the appropriate decoding policy to the raw response body stream.
 * When constructed from a raw byte array containing the response headers, it
 * composes a {@link LengthPolicy} with a {@link ChunkPolicy}, falling back to
 * an {@link UnsupportedPolicy} if neither mechanism is recognized.
 * <p>
 * A {@code BodyPolicy} instance can also be created by wrapping an existing
 * {@code UnaryOperator<IntStream>}, in which case it simply delegates to that
 * operator.
 */
public final class BodyPolicy implements UnaryOperator<IntStream> {

    /**
     * The underlying policy that performs the actual transformation.
     */
    private final UnaryOperator<IntStream> origin;

    /**
     * Creates a body decoding policy based on the given raw response bytes,
     * which are expected to contain the response headers.
     * <p>
     * The policy first attempts to determine the body length from the response
     * headers via {@link LengthPolicy}. If a {@code Content-Length} header is
     * present, that length is used to read exactly the specified number of
     * bytes. Otherwise, a {@link ChunkPolicy} is applied to handle chunked
     * transfer encoding. If neither mechanism applies, an
     * {@link UnsupportedPolicy} is used, which typically fails or returns the
     * stream unchanged depending on its implementation.
     *
     * @param bs the raw response bytes containing the headers used to
     *           determine the body encoding
     */
    public BodyPolicy(final byte[] bs) {
        this(
            new LengthPolicy(
                bs,
                new ChunkPolicy(
                    bs,
                    new UnsupportedPolicy()
                )
            )
        );
    }

    /**
     * Creates a body decoding policy by wrapping an existing stream
     * transformation operator.
     *
     * @param op the underlying operator applied to the body stream
     */
    public BodyPolicy(final UnaryOperator<IntStream> op) {
        this.origin = op;
    }

    /**
     * Applies the decoding policy to the given response body stream.
     * <p>
     * The transformation is delegated to the underlying operator, which
     * applies the appropriate decoding logic (length-based, chunked, or
     * unsupported) as determined at construction time.
     *
     * @param s the raw response body stream to decode
     * @return the decoded body stream
     */
    @Override
    public IntStream apply(final IntStream s) {
        return this.origin.apply(s);
    }
}
