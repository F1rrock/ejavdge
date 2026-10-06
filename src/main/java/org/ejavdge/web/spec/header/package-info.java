/**
 * HTTP header fields.
 * <p>
 * This package provides the abstractions for building and attaching HTTP
 * headers to a request or response message. The two central types are:
 * </p>
 * <ul>
 *   <li>{@link org.ejavdge.web.spec.header.Header} – a
 *       {@link org.ejavdge.scalar.bytes.Bytes} that represents a single
 *       header line, formatted as {@code "<name>: <value>"} and terminated
 *       by a line break. Values can be supplied as
 *       {@link org.ejavdge.scalar.text.Text} or
 *       {@link org.ejavdge.scalar.num.Num}; in the latter case they are
 *       converted to their decimal representation. A precomputed byte
 *       sequence can also be wrapped directly, which is useful when
 *       reconstructing a header block from an existing payload;</li>
 *   <li>{@link org.ejavdge.web.spec.header.WithHeaders} – an
 *       {@link org.ejavdge.web.spec.HttpSpec} decorator that appends one or
 *       more {@code Header} instances to an existing HTTP message. Each
 *       header already carries its own terminating line break, so the
 *       resulting byte sequence is a valid HTTP message with the additional
 *       headers appended to the existing header block. The underlying spec
 *       is required to be non-empty, so that headers cannot be emitted on
 *       their own without a preceding request or status line.</li>
 * </ul>
 * <p>
 * Headers are combined with the rest of the HTTP spec hierarchy — the
 * request or status line, the body, and the terminator that separates
 * headers from the body — through the decorators in
 * {@link org.ejavdge.web.spec}. This allows a complete request to be built
 * lazily from reusable pieces: for example, a request line can be wrapped
 * with a {@code Host} header, a {@code Content-Type} header, and a
 * {@code Content-Length} header, and then combined with a body via
 * {@link org.ejavdge.web.spec.body.WithBody}. All of this remains
 * unevaluated until the resulting message is materialized for sending. If
 * an invariant is violated at that point — for example, the underlying
 * spec is empty or a header name is malformed — an
 * {@link org.ejavdge.error.InvariantViolation} is thrown.
 * </p>
 */
package org.ejavdge.web.spec.header;
