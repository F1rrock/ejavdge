/**
 * HTTP/1.1 request line and message assembly.
 * <p>
 * This package provides the top-level abstractions for building HTTP
 * messages lazily and materializing them on demand. The central type is
 * {@link org.ejavdge.web.spec.HttpSpec}, a functional interface that
 * represents a fragment of an HTTP message and produces its raw bytes via
 * {@link org.ejavdge.web.spec.HttpSpec#bytes()}. Because specs are lazy,
 * a complete request can be described declaratively and assembled from
 * reusable pieces, and no bytes are computed until the driver is ready to
 * send the message.
 * <p>
 * The package is organized around the following types:
 * </p>
 * <ul>
 *   <li>{@link org.ejavdge.web.spec.HttpSpec} – the functional interface
 *       for an HTTP message fragment, with a nested
 *       {@link org.ejavdge.web.spec.HttpSpec.Of HttpSpec.Of} implementation
 *       that wraps a fixed {@link org.ejavdge.scalar.bytes.Bytes} value;</li>
 *   <li>{@link org.ejavdge.web.spec.ByteView} – an adapter that exposes an
 *       {@code HttpSpec} as a {@link org.ejavdge.scalar.bytes.Bytes}, so
 *       that message fragments can be memoized, concatenated, measured, or
 *       validated through the same combinators used for other byte
 *       content;</li>
 *   <li>{@link org.ejavdge.web.spec.Terminator} – the standard HTTP line
 *       ending {@code CRLF} ({@code "\r\n"}), used as a reusable byte
 *       sequence for separating lines and closing messages;</li>
 *   <li>{@link org.ejavdge.web.spec.Request} – a complete HTTP request
 *       assembled from any {@code HttpSpec} by appending a final
 *       terminator. This is the top-level entry point of the spec
 *       hierarchy.</li>
 * </ul>
 * <p>
 * Concrete message fragments are provided by the sibling subpackages, which
 * compose with the abstractions defined here:
 * </p>
 * <ul>
 *   <li>{@link org.ejavdge.web.spec.method} – HTTP request templates for
 *       {@code GET} and {@code POST}, producing the request line and the
 *       {@code Host} header;</li>
 *   <li>{@link org.ejavdge.web.spec.header} – header construction
 *       ({@code Header}) and header attachment
 *       ({@code WithHeaders});</li>
 *   <li>{@link org.ejavdge.web.spec.body} – body attachment
 *       ({@code WithBody}), which adds the {@code Content-Length} header
 *       and the body itself;</li>
 *   <li>{@link org.ejavdge.web.spec.body.multipart} – the building blocks
 *       for {@code multipart/form-data} payloads, including parts,
 *       boundaries, and a complete {@code Multipart} spec.</li>
 * </ul>
 * <p>
 * A typical request is assembled by starting from a method template
 * (for example {@code Post}), wrapping it with one or more headers (for
 * example {@code Content-Type}), attaching a body (for example a
 * {@code Form} or a {@code Multipart}), and finally closing it with a
 * {@code Request}. All of this remains unevaluated until
 * {@code bytes()} is invoked. If an invariant is violated at that point —
 * for example, a required value is empty or a body source cannot be read —
 * an {@link org.ejavdge.error.InvariantViolation} is thrown.
 * </p>
 */
package org.ejavdge.web.spec;
