package org.ejavdge.web.driver.jdk.socket.body;

import org.ejavdge.scalar.num.Num;

import java.util.function.UnaryOperator;
import java.util.stream.IntStream;

/**
 * A body decoding policy that limits the response body stream to a specified
 * number of bytes.
 * <p>
 * This class implements {@link UnaryOperator UnaryOperator&lt;IntStream&gt;}
 * and wraps a {@link Num} representing the maximum number of bytes to read from
 * the body stream. When {@link #apply(IntStream)} is called, the input stream
 * is limited to that many bytes using {@link IntStream#limit(long)}. This is
 * the mechanism used to honor the {@code Content-Length} header of an HTTP
 * response: once the declared number of bytes has been read, the stream is
 * exhausted and no further bytes are consumed.
 * <p>
 * The size is supplied as a {@link Num} so that it can be computed lazily or
 * derived from another source (such as the parsed {@code Content-Length}
 * header) at the time the policy is constructed.
 */
public final class WithLimitation implements UnaryOperator<IntStream> {

    /**
     * The maximum number of bytes to read from the body stream.
     */
    final Num size;

    /**
     * Creates a limiting policy with the given maximum size.
     *
     * @param n the maximum number of bytes to read from the body stream
     */
    public WithLimitation(final Num n) {
        this.size = n;
    }

    /**
     * Applies the size limit to the given body stream.
     * <p>
     * The input stream is limited to the number of bytes specified by
     * {@link Num#value()}. If the stream contains fewer bytes than the limit,
     * it is exhausted normally; if it contains more, the excess bytes are left
     * unread.
     *
     * @param s the raw response body stream to limit
     * @return a stream containing at most {@code size} bytes from the input
     *         stream
     */
    @Override
    public IntStream apply(final IntStream s) {
        return s.limit(this.size.value());
    }
}
