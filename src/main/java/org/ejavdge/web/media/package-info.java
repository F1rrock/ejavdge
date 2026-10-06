/**
 * Cookies, forms, and multipart helpers for ejudge pages.
 * <p>
 * This package provides the {@link org.ejavdge.web.media.Media} abstraction and
 * its implementations for building the body and header payloads of HTTP
 * requests sent to the ejudge contest system. A media represents the target of
 * a {@link org.ejavdge.web.context.Context} imprint operation: it accumulates
 * named entries via {@link org.ejavdge.web.media.Media#with} and materializes
 * them into a final value of a specific type via
 * {@link org.ejavdge.web.media.Media#content()}.
 * </p>
 * <p>
 * The package contains the following media implementations:
 * </p>
 * <ul>
 *   <li>{@link org.ejavdge.web.media.Cookies} – accumulates cookie name-value
 *       pairs and renders them as a {@code "; "}-separated string suitable for
 *       the HTTP {@code Cookie} header. Both names and values are URL-encoded
 *       via {@link org.ejavdge.scalar.text.PartOfUrl};</li>
 *   <li>{@link org.ejavdge.web.media.Form} – accumulates form fields and
 *       renders them as a UTF-8 byte array in the
 *       {@code application/x-www-form-urlencoded} format, with entries
 *       separated by {@code "&"} and both names and values URL-encoded;</li>
 *   <li>{@link org.ejavdge.web.media.Gist} – collects only the values from a
 *       context, discarding the associated names, and exposes them as a
 *       {@link java.util.List} of {@link org.ejavdge.scalar.text.Text}
 *       values;</li>
 *   <li>{@link org.ejavdge.web.media.WhiteList} – a decorator that filters
 *       named entries, forwarding to the underlying media only those whose
 *       names appear in a configured whitelist;</li>
 *   <li>{@link org.ejavdge.web.media.Lift} – a decorator used internally by
 *       {@link org.ejavdge.web.context.Union} to allow two contexts to be
 *       applied in sequence by lifting an intermediate media value into the
 *       outer media position.</li>
 * </ul>
 * <p>
 * In addition, the nested {@code ImprintOf} classes provided by
 * {@code Cookies}, {@code Form}, and {@code Gist} offer convenient ways to
 * render a {@link org.ejavdge.web.context.Context} directly into the
 * corresponding media format. For example, a context carrying credentials can
 * be imprinted onto a {@code Form} to produce a login request body, or a
 * context carrying a session token can be imprinted onto {@code Cookies} to
 * produce a {@code Cookie} header value.
 * </p>
 * <p>
 * Multipart form payloads, which are required for submitting solutions with
 * attached files, are handled by the companion package
 * {@link org.ejavdge.web.spec.body.multipart} and its supporting types
 * ({@code Multipart}, {@code Part}, {@code FilePart}, {@code TextParts}).
 * Those types integrate with the media abstractions defined here through the
 * {@link org.ejavdge.web.spec.Request} and
 * {@link org.ejavdge.contest.ContestForm} classes.
 * </p>
 */
package org.ejavdge.web.media;
