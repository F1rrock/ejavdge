/**
 * HTTP method request templates.
 * <p>
 * This package provides {@link org.ejavdge.web.spec.HttpSpec} implementations
 * that produce the opening line of an HTTP request and the minimal set of
 * headers required for that request to be valid. Each class corresponds to a
 * specific HTTP method and is responsible only for the first line and the
 * {@code Host} header; additional headers, a body, and the terminator
 * separating headers from the body are attached by the decorators in the
 * sibling packages.
 * </p>
 * <p>
 * The package contains the following request templates:
 * </p>
 * <ul>
 *   <li>{@link org.ejavdge.web.spec.method.Get} – an HTTP {@code GET}
 *       request opening, consisting of the request line
 *       {@code GET <url> HTTP/1.1} followed by a
 *       {@code Host: <host>:<port>} header;</li>
 *   <li>{@link org.ejavdge.web.spec.method.Post} – an HTTP {@code POST}
 *       request opening, consisting of the request line
 *       {@code POST <url> HTTP/1.1} followed by a
 *       {@code Host: <host>:<port>} header.</li>
 * </ul>
 * <p>
 * Both classes accept the target of the request either as a fully formed
 * {@link org.ejavdge.web.context.Location} or as a URL path, host, and port
 * separately. Internally, the location's components (URL, host, port) are
 * extracted via {@link org.ejavdge.web.media.Gist.ImprintOf} and wrapped
 * with {@link org.ejavdge.scalar.text.NonEmpty}, so that any empty
 * component is reported as an
 * {@link org.ejavdge.error.InvariantViolation} when the request is
 * materialized.
 * </p>
 * <p>
 * Because these classes are themselves
 * {@link org.ejavdge.web.spec.HttpSpec} instances, they can be composed
 * with the header and body decorators from
 * {@link org.ejavdge.web.spec.header} and
 * {@link org.ejavdge.web.spec.body} to build complete HTTP messages lazily.
 * The final bytes are produced only when
 * {@link org.ejavdge.web.spec.HttpSpec#bytes()} is invoked, typically by
 * the web driver just before sending the request.
 * </p>
 */
package org.ejavdge.web.spec.method;
