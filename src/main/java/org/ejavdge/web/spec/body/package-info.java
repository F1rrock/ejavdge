/**
 * HTTP message bodies.
 * <p>
 * This package provides the abstraction for attaching a payload — the body
 * of a request or response — to an HTTP message. Its central type is
 * {@link org.ejavdge.web.spec.body.WithBody}, an
 * {@link org.ejavdge.web.spec.HttpSpec} decorator that takes a body as
 * {@link org.ejavdge.scalar.bytes.Bytes} and an existing HTTP spec, and
 * produces a complete message consisting of the underlying spec augmented
 * with a {@code Content-Length} header, a terminator separating the headers
 * from the body, and the body itself.
 * </p>
 * <p>
 * The body is memoized inside {@code WithBody}, so its bytes are computed
 * at most once even though the class needs to inspect them twice — once to
 * determine the length for the {@code Content-Length} header, and once to
 * emit them after the terminator. This makes the decorator safe to use with
 * expensive or non-idempotent body sources, such as lazily assembled
 * payloads or network responses.
 * </p>
 * <p>
 * Concrete body encodings live in the subpackage
 * {@link org.ejavdge.web.spec.body.multipart}, which provides the building
 * blocks for {@code multipart/form-data} payloads — parts, boundaries,
 * prefix and suffix decorators, and a complete
 * {@link org.ejavdge.web.spec.body.multipart.Multipart} spec that assembles
 * a list of parts into a full HTTP message.
 * </p>
 * <p>
 * Together, these classes allow request bodies to be described
 * declaratively and composed with the rest of the HTTP spec hierarchy
 * (request line, headers, body), so that a complete request can be built
 * lazily and materialized only when the driver is ready to send it. If an
 * invariant is violated during materialization — for example, the body is
 * empty or a part header is malformed — an
 * {@link org.ejavdge.error.InvariantViolation} is thrown.
 * </p>
 */
package org.ejavdge.web.spec.body;
