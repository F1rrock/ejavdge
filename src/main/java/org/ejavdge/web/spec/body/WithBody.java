package org.ejavdge.web.spec.body;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.*;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.web.spec.ByteView;
import org.ejavdge.web.spec.HttpSpec;
import org.ejavdge.web.spec.Terminator;
import org.ejavdge.web.spec.header.Header;
import org.ejavdge.web.spec.header.WithHeaders;

/**
 * An {@link HttpSpec} decorator that attaches a request body to an
 * existing HTTP message.
 * <p>
 * Given a body as {@link Bytes} and an underlying HTTP spec (typically a
 * request line with headers), this class produces a complete HTTP message
 * that consists of:
 * <ul>
 *   <li>the underlying spec, augmented with a {@code Content-Length}
 *       header whose value is the size of the body in bytes;</li>
 *   <li>a {@link Terminator} that separates the headers from the body;</li>
 *   <li>the body itself.</li>
 * </ul>
 * <p>
 * The body is memoized via {@link Memo}, so its bytes are computed at most
 * once even though the class needs to inspect them both to determine the
 * length for the {@code Content-Length} header and to emit them after the
 * terminator. This makes it safe to use with expensive or non-idempotent
 * body sources, such as network responses or lazily assembled payloads.
 * <p>
 * If the body is empty, {@link NonEmpty} causes the underlying spec to be
 * materialized anyway and an {@link InvariantViolation} to be thrown,
 * ensuring that requests with no body content are reported as errors
 * rather than silently producing a malformed message.
 */
public final class WithBody implements HttpSpec {

    /**
     * The fully assembled HTTP message (headers, terminator, and body).
     */
    private final Bytes src;

    /**
     * Creates a new spec that attaches the given body to the underlying
     * HTTP message.
     * <p>
     * The body is memoized and a {@code Content-Length} header is added to
     * the underlying spec, whose value is the body's size in bytes. The
     * final message is the concatenation of the augmented spec, a
     * terminator, and the body.
     *
     * @param b the request body to attach
     * @param s the underlying HTTP spec that provides the request line and
     *          any pre-existing headers
     */
    public WithBody(final Bytes b, final HttpSpec s) {
        final var body = new Memo(b);
        this.src = new Concat(
            new NonEmpty(
                new ByteView(
                    new WithHeaders(
                        new Header(
                            new Text.Of("Content-Length"),
                            new Size(body)
                        ),
                        s
                    )
                )
            ),
            new Terminator(),
            body
        );
    }

    /**
     * Returns the raw bytes of the assembled HTTP message, including the
     * {@code Content-Length} header, the terminator, and the body.
     *
     * @return the HTTP message as a byte array
     * @throws InvariantViolation if an invariant is violated while
     *         materializing the message, for example if the underlying
     *         spec is empty or the body cannot be read
     */
    @Override
    public byte[] bytes() throws InvariantViolation {
        return this.src.content();
    }
}
