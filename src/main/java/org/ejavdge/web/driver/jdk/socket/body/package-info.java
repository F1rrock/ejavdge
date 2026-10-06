/**
 * HTTP response body reading policies.
 * <p>
 * This package provides the decoding policies used by the JDK socket-based web
 * driver to correctly read the body of an HTTP response. HTTP responses can
 * encode their bodies in different ways, and these policies determine how to
 * interpret the raw byte stream coming from the socket.
 * </p>
 * <p>
 * The package is built around the
 * {@link java.util.function.UnaryOperator UnaryOperator&lt;IntStream&gt;}
 * abstraction: each policy transforms the raw body stream into a decoded stream
 * of bytes. The policies are composed in a chain and applied in order until one
 * of them recognizes the encoding used by the response:
 * </p>
 * <ul>
 *   <li>{@link org.ejavdge.web.driver.jdk.socket.body.LengthPolicy} – handles
 *       responses that specify their body size via the {@code Content-Length}
 *       header, limiting the stream to the declared number of bytes using
 *       {@link org.ejavdge.web.driver.jdk.socket.body.WithLimitation};</li>
 *   <li>{@link org.ejavdge.web.driver.jdk.socket.body.ChunkPolicy} – handles
 *       responses that use {@code Transfer-Encoding: chunked}, decoding the
 *       chunked stream using
 *       {@link org.ejavdge.web.driver.jdk.socket.body.WithChunks};</li>
 *   <li>{@link org.ejavdge.web.driver.jdk.socket.body.UnsupportedPolicy} – the
 *       terminal fallback that throws an
 *       {@link org.ejavdge.error.InvariantViolation} when the response body
 *       structure cannot be recognized.</li>
 * </ul>
 * <p>
 * The composition of these policies is encapsulated in
 * {@link org.ejavdge.web.driver.jdk.socket.body.BodyPolicy}, which inspects the
 * raw response headers and selects the appropriate decoding strategy. The
 * resulting decoded body is then exposed as
 * {@link org.ejavdge.scalar.bytes.Bytes} via
 * {@link org.ejavdge.web.driver.jdk.socket.body.BodyOf}.
 * </p>
 * <p>
 * Supporting classes include:
 * </p>
 * <ul>
 *   <li>{@link org.ejavdge.web.driver.jdk.socket.body.WithLimitation} – a
 *       simple stream transformation that limits the body to a given size;</li>
 *   <li>{@link org.ejavdge.web.driver.jdk.socket.body.WithChunks} – a
 *       recursive decoder for chunked transfer encoding.</li>
 * </ul>
 * <p>
 * Together, these classes ensure that HTTP response bodies are read correctly
 * regardless of the encoding mechanism used by the server, and that
 * unrecognized encodings are reported as explicit errors rather than being
 * silently mishandled.
 * </p>
 */
package org.ejavdge.web.driver.jdk.socket.body;
