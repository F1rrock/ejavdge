package org.ejavdge.web.driver.jdk.socket.body;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.web.driver.jdk.socket.HttpResponse;
import org.ejavdge.web.driver.jdk.stream.BytesOfStream;

import java.util.function.UnaryOperator;
import java.util.stream.IntStream;

/**
 * A {@link Bytes} view over the body of an HTTP response, with a configurable
 * transformation policy applied to the underlying stream of bytes.
 * <p>
 * This class implements {@link Bytes} and wraps an {@link HttpResponse} and a
 * {@link UnaryOperator UnaryOperator&lt;IntStream&gt;} policy. When
 * {@link #content()} is called, the response's body stream is first obtained,
 * then passed through the policy (which may, for example, decode content
 * encodings such as gzip or deflate, or otherwise filter or transform the
 * bytes), and finally converted into a raw byte array via
 * {@link BytesOfStream}.
 * <p>
 * This is useful for handling HTTP responses whose bodies are encoded or
 * otherwise need to be processed before being exposed as plain bytes to the
 * rest of the application.
 */
public final class BodyOf implements Bytes {

    /**
     * The HTTP response whose body is exposed.
     */
    private final HttpResponse src;

    /**
     * The policy that transforms the response body stream before conversion to
     * bytes.
     */
    private final UnaryOperator<IntStream> policy;

    /**
     * Creates a new byte view over the given HTTP response body, applying the
     * specified transformation policy.
     *
     * @param r the HTTP response whose body is exposed
     * @param o the policy that transforms the response body stream before it
     *          is converted to bytes
     */
    public BodyOf(final HttpResponse r, final UnaryOperator<IntStream> o) {
        this.src = r;
        this.policy = o;
    }

    /**
     * Returns the raw byte content of the HTTP response body after applying the
     * configured transformation policy.
     * <p>
     * The response's body stream is obtained via {@link HttpResponse#body()},
     * transformed by the policy, and then converted into a byte array using
     * {@link BytesOfStream}.
     *
     * @return the transformed body content as a byte array
     * @throws InvariantViolation if an invariant is violated while reading the
     *         body or applying the policy
     */
    @Override
    public byte[] content() throws InvariantViolation {
        return new BytesOfStream(
            this.policy.apply(this.src.body())
        ).content();
    }
}
