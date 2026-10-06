/**
 * Stream utilities for HTTP payloads.
 * <p>
 * This package provides the low-level stream processing utilities used by the
 * JDK socket-based web driver to read and manipulate raw HTTP payloads. It is
 * built around a small set of composable abstractions for working with streams
 * of byte values, allowing the driver to process responses lazily and without
 * buffering entire payloads in memory.
 * </p>
 * <p>
 * The central abstraction is the
 * {@link org.ejavdge.web.driver.jdk.stream.ByteStream} interface, a functional
 * interface representing a lazy source of bytes (each element being an
 * {@code int} in the range {@code 0} to {@code 255}). Around this interface,
 * the package provides the following utilities:
 * </p>
 * <ul>
 *   <li>{@link org.ejavdge.web.driver.jdk.stream.Lines} – splits a byte stream
 *       into individual lines, each represented as an {@code int[]} of byte
 *       values, without the separator. This is used to parse HTTP header
 *       blocks and chunked transfer-encoded bodies line by line;</li>
 *   <li>{@link org.ejavdge.web.driver.jdk.stream.ConcatOfLines} – the inverse
 *       of {@code Lines}, concatenating a stream of lines back into a single
 *       continuous byte stream with a configurable separator after each line;</li>
 *   <li>{@link org.ejavdge.web.driver.jdk.stream.Tee} – duplicates a stream
 *       into two independently consumable branches, buffering elements as
 *       needed. This is the key primitive that allows an HTTP response to be
 *       parsed in a single pass while simultaneously extracting both the
 *       header block and the body;</li>
 *   <li>{@link org.ejavdge.web.driver.jdk.stream.BytesOfStream} – collects an
 *       {@code IntStream} of byte values into a raw byte array, exposed as
 *       {@link org.ejavdge.scalar.bytes.Bytes};</li>
 *   <li>{@link org.ejavdge.web.driver.jdk.stream.BytesOfLine} – the
 *       single-line counterpart of {@code BytesOfStream}, typically used to
 *       convert an extracted line into bytes for further processing.</li>
 * </ul>
 * <p>
 * Together, these utilities allow the socket-based web driver to parse HTTP
 * responses in a fully streaming fashion: the raw byte stream from the socket
 * is duplicated with a {@code Tee}, the header block is split into lines with
 * {@code Lines} and stopped at the first empty line, and the body is read from
 * the other branch, skipping past the header block. The extracted lines and
 * body fragments can then be converted to bytes with {@code BytesOfStream} or
 * {@code BytesOfLine}, decoded as UTF-8, or passed on to the body decoding
 * policies in {@link org.ejavdge.web.driver.jdk.socket.body}.
 * </p>
 */
package org.ejavdge.web.driver.jdk.stream;
