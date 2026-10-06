/**
 * Multipart form encoding.
 * <p>
 * This package provides the building blocks for constructing
 * {@code multipart/form-data} request bodies, as used when submitting
 * solutions with attached files or any other payload that mixes text fields
 * with binary content. The encoding is assembled from a small set of
 * composable abstractions that mirror the structure of the wire format.
 * </p>
 * <p>
 * The package is organized around the following types:
 * </p>
 * <ul>
 *   <li>{@link org.ejavdge.web.spec.body.multipart.Part} – the contract for a
 *       single part inside a multipart body, responsible for producing its
 *       own bytes (including any per-part headers but excluding surrounding
 *       boundaries). A generic {@code Part.Of} adapter is provided for
 *       wrapping precomputed {@link org.ejavdge.scalar.bytes.Bytes};</li>
 *   <li>{@link org.ejavdge.web.spec.body.multipart.TextPart} – a part that
 *       represents a plain text form field, with a
 *       {@code Content-Disposition} header and a UTF-8 encoded value;</li>
 *   <li>{@link org.ejavdge.web.spec.body.multipart.FilePart} – a part that
 *       represents a file attachment, with a {@code Content-Disposition}
 *       header carrying the file name, a {@code Content-Type} header of
 *       {@code application/octet-stream}, and the raw file bytes as the
 *       body;</li>
 *   <li>{@link org.ejavdge.web.spec.body.multipart.TextParts} – a
 *       {@link org.ejavdge.web.media.Media} that accumulates text fields as
 *       parts, analogous to how
 *       {@link org.ejavdge.web.media.Form} accumulates form-encoded fields.
 *       The nested {@code ImprintOf} class converts a
 *       {@link org.ejavdge.web.context.Context} into a list of parts;</li>
 *   <li>{@link org.ejavdge.web.spec.body.multipart.Boundary} – the MIME
 *       boundary marker used to delimit the parts, generated in the
 *       WebKit style by default;</li>
 *   <li>{@link org.ejavdge.web.spec.body.multipart.WithPrefix} and
 *       {@link org.ejavdge.web.spec.body.multipart.WithSuffix} – byte-level
 *       decorators that prepend or append a boundary marker to a byte
 *       sequence, used to construct opening and closing delimiters;</li>
 *   <li>{@link org.ejavdge.web.spec.body.multipart.BytesOfPart} – an adapter
 *       that exposes a {@code Part} as
 *       {@link org.ejavdge.scalar.bytes.Bytes}, allowing parts to be
 *       concatenated with the same combinators used for other byte
 *       sequences;</li>
 *   <li>{@link org.ejavdge.web.spec.body.multipart.Multipart} – an
 *       {@link org.ejavdge.web.spec.HttpSpec} that assembles a list of
 *       parts into a complete multipart HTTP message, including the
 *       top-level {@code Content-Type} header with the boundary
 *       parameter.</li>
 * </ul>
 * <p>
 * The parts and the boundary are combined lazily: the actual byte
 * concatenation happens when the resulting message is materialized via
 * {@link org.ejavdge.web.spec.HttpSpec#bytes()}. This makes it easy to
 * build up complex payloads — for example, a solution submission that
 * combines credential fields, a language id, and the source file — without
 * materializing any bytes until the request is actually sent. If an
 * invariant is violated (for example, a part header contains an empty
 * name), an {@link org.ejavdge.error.InvariantViolation} is thrown when
 * the affected part is evaluated.
 * </p>
 */
package org.ejavdge.web.spec.body.multipart;
