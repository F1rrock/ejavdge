package org.ejavdge.web.driver.jdk.socket.body;

import org.ejavdge.error.InvariantViolation;

import java.util.function.UnaryOperator;
import java.util.stream.IntStream;

/**
 * A terminal body decoding policy that reports an unsupported response body
 * structure.
 * <p>
 * This class implements {@link UnaryOperator UnaryOperator&lt;IntStream&gt;}
 * and represents the last resort in a chain of body decoding policies. It is
 * used when none of the preceding policies — such as {@link LengthPolicy} for
 * {@code Content-Length} and {@link ChunkPolicy} for chunked transfer
 * encoding — can determine how to decode the response body.
 * <p>
 * When {@link #apply(IntStream)} is called, it does not attempt to transform
 * the stream. Instead, it always throws an {@link InvariantViolation} with the
 * message {@code "Unknown response body structure."}, signalling that the
 * response body cannot be interpreted by the current set of policies.
 */
public final class UnsupportedPolicy implements UnaryOperator<IntStream> {

    /**
     * The error message used when this policy is applied.
     */
    private final String message;

    /**
     * Creates a new unsupported policy with the default error message.
     * <p>
     * The default message is {@code "Unknown response body structure."}.
     */
    public UnsupportedPolicy() {
        this.message = "Unknown response body structure.";
    }

    /**
     * Always throws an {@link InvariantViolation}, indicating that the response
     * body structure is not supported.
     * <p>
     * This method does not attempt to process the given stream. It is intended
     * as a terminal fallback in a chain of body decoding policies, so that an
     * unrecognized response body is reported as an explicit error rather than
     * being silently mishandled.
     *
     * @param s the raw response body stream, which is not processed
     * @return never returns normally
     * @throws InvariantViolation always, with the message
     *         {@code "Unknown response body structure."}
     */
    @Override
    public IntStream apply(final IntStream s) {
        throw new InvariantViolation(this.message);
    }
}
