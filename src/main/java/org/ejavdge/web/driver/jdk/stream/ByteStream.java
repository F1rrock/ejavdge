package org.ejavdge.web.driver.jdk.stream;

import java.util.stream.IntStream;

/**
 * A contract for a stream of byte values.
 * <p>
 * This is a functional interface representing a lazy source of bytes, where
 * each element of the stream is an {@code int} in the range {@code 0} to
 * {@code 255}. It is used by the socket-based web driver as the fundamental
 * abstraction for reading raw data from an input source, such as a network
 * socket.
 * <p>
 * This is a functional interface whose functional method is {@link #content()}.
 * Unlike {@link org.ejavdge.scalar.bytes.Bytes}, which represents a fully
 * materialized byte array, a {@code ByteStream} is inherently streaming: the
 * bytes are produced on demand, and the stream may be infinite or unbounded.
 * This makes it suitable for reading data whose total size is not known in
 * advance, such as an HTTP response body.
 */
@FunctionalInterface
public interface ByteStream {

    /**
     * Returns the underlying content as a stream of byte values.
     * <p>
     * Each element of the returned {@link IntStream} is expected to represent
     * a single byte, with a value in the range {@code 0} to {@code 255}.
     * Callers are responsible for consuming the stream and, if applicable,
     * closing it to release any underlying resources.
     *
     * @return the stream of byte values
     */
    IntStream content();

    /**
     * A simple {@link ByteStream} implementation that wraps a fixed
     * {@link IntStream}.
     * <p>
     * The stream is provided at construction time and returned unchanged by
     * {@link #content()}. This is useful for adapting an existing
     * {@code IntStream} to the {@code ByteStream} interface without any
     * additional processing.
     */
    final class Of implements ByteStream {

        /**
         * The wrapped stream of byte values.
         */
        private final IntStream src;

        /**
         * Creates a byte stream from the given {@link IntStream}.
         *
         * @param rs the stream of byte values to wrap
         */
        public Of(final IntStream rs) {
            this.src = rs;
        }

        /**
         * Returns the wrapped stream of byte values.
         *
         * @return the underlying {@link IntStream}
         */
        @Override
        public IntStream content() {
            return this.src;
        }
    }
}
