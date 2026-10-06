package org.ejavdge.web.spec;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;

/**
 * A contract for a fragment of an HTTP message, represented as raw bytes.
 * <p>
 * An {@code HttpSpec} is a lazy description of some portion of an HTTP
 * request or response — for example, the request line, a set of headers,
 * a body, or a complete message assembled from these parts. The bytes are
 * produced on demand via {@link #bytes()}, which allows a message to be
 * built up declaratively from reusable pieces without materializing any
 * bytes until the message is actually ready to be sent.
 * <p>
 * This is a functional interface whose functional method is
 * {@link #bytes()}. The nested {@link Of} class provides a convenient
 * implementation that wraps an existing {@link Bytes} value (or a raw
 * {@code byte[]}), making it easy to treat precomputed content as an
 * {@code HttpSpec} and to compose it with other specs using the decorators
 * in {@link org.ejavdge.web.spec.header} and
 * {@link org.ejavdge.web.spec.body}.
 */
@FunctionalInterface
public interface HttpSpec {

    /**
     * Returns the raw bytes of this HTTP message fragment.
     * <p>
     * Implementations may perform lazy assembly, concatenation, or other
     * processing as part of this call. If an invariant is violated — for
     * example, a required header value is missing or empty — an
     * {@link InvariantViolation} is thrown.
     *
     * @return the HTTP message fragment as a byte array
     * @throws InvariantViolation if an invariant is violated while
     *         materializing the message
     */
    byte[] bytes() throws InvariantViolation;

    /**
     * A simple {@link HttpSpec} implementation that wraps a fixed
     * {@link Bytes} value.
     * <p>
     * This is useful when the bytes of an HTTP fragment are already
     * available — for example, when adapting a precomputed request or a
     * test fixture — and need to be treated as an {@code HttpSpec} so they
     * can be combined with other specs. The {@link #bytes()} method
     * simply delegates to the underlying {@link Bytes#content()}.
     */
    final class Of implements HttpSpec {

        /**
         * The underlying byte content of the HTTP fragment.
         */
        final Bytes src;

        /**
         * Creates an HTTP spec from the given raw bytes.
         *
         * @param src the raw bytes of the HTTP fragment
         */
        public Of(final byte[] src) {
            this(new Bytes.Of(src));
        }

        /**
         * Creates an HTTP spec from the given byte sequence.
         *
         * @param src the byte content of the HTTP fragment
         */
        public Of(final Bytes src) {
            this.src = src;
        }

        /**
         * Returns the raw bytes of the underlying {@link Bytes} value.
         *
         * @return the HTTP fragment as a byte array
         */
        @Override
        public byte[] bytes() {
            return this.src.content();
        }
    }
}
