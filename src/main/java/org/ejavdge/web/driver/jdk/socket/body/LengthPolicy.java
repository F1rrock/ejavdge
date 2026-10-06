package org.ejavdge.web.driver.jdk.socket.body;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.web.resource.ContentLength;

import java.util.function.UnaryOperator;
import java.util.stream.IntStream;

/**
 * A body decoding policy that handles responses with a {@code Content-Length}
 * header.
 * <p>
 * When an HTTP response specifies its body length via the
 * {@code Content-Length} header, exactly that many bytes follow the headers.
 * This class implements a {@link UnaryOperator UnaryOperator&lt;IntStream&gt;}
 * that reads the value of the {@code Content-Length} header from the raw
 * response bytes and, if present, limits the body stream to that many bytes
 * using {@link WithLimitation}.
 * <p>
 * If the {@code Content-Length} header is missing or cannot be parsed, the
 * policy delegates to a fallback operator supplied at construction time —
 * typically a policy that handles chunked transfer encoding or reports that
 * the encoding is unsupported.
 */
public final class LengthPolicy implements UnaryOperator<IntStream> {

    /**
     * The value of the {@code Content-Length} header, parsed as a number.
     */
    private final Num len;

    /**
     * The fallback policy applied when no valid {@code Content-Length} header
     * is present.
     */
    private final UnaryOperator<IntStream> next;

    /**
     * Creates a length-based decoding policy from the given raw response bytes
     * and fallback operator.
     * <p>
     * The {@code Content-Length} header is extracted from the raw response
     * bytes via {@link ContentLength} and interpreted as a numeric value.
     *
     * @param bs  the raw response bytes containing the headers used to
     *            determine the body length
     * @param nxt the fallback operator to apply if the {@code Content-Length}
     *            header is not available or cannot be parsed
     */
    public LengthPolicy(final byte[] bs, final UnaryOperator<IntStream> nxt) {
        this.len = new ContentLength(
            new Bytes.Of(bs)
        );
        this.next = nxt;
    }

    /**
     * Applies this policy to the given response body stream.
     * <p>
     * The method attempts to read the value of the {@code Content-Length}
     * header. If the value can be obtained, the body stream is limited to that
     * many bytes via {@link WithLimitation}. If the header is missing or cannot
     * be parsed (i.e., an {@link InvariantViolation} is thrown), the fallback
     * operator is applied to the stream instead.
     *
     * @param s the raw response body stream
     * @return the body stream, either limited to the specified length or
     *         handled by the fallback operator
     */
    @Override
    public IntStream apply(final IntStream s) {
        int size;
        try {
            size = this.len.value();
        } catch (final InvariantViolation e) {
            return this.next.apply(s);
        }
        return new WithLimitation(
            new Num.Of(size)
        ).apply(s);
    }
}
