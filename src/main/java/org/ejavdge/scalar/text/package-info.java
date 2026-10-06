/**
 * Text values, templates, and string operations.
 * <p>
 * This package provides the central abstraction for working with textual data
 * throughout the application: the {@link org.ejavdge.scalar.text.Text}
 * interface. A {@code Text} instance represents an immutable UTF-16 string that
 * is materialized on demand via its
 * {@link org.ejavdge.scalar.text.Text#content()} method. This allows text
 * values to be composed, transformed, and decorated lazily, deferring expensive
 * work (such as network access, parsing, or extraction from an HTML document)
 * until the result is actually needed.
 * </p>
 * <p>
 * The package is organized around the {@code Text} interface and a rich set of
 * implementations and decorators that cover common patterns for handling
 * textual data:
 * </p>
 * <ul>
 *   <li><b>Core abstraction:</b>
 *       <ul>
 *         <li>{@link org.ejavdge.scalar.text.Text} – the functional interface
 *             for a lazy text value, with a nested
 *             {@link org.ejavdge.scalar.text.Text.Of Text.Of} implementation
 *             that wraps a fixed string.</li>
 *       </ul>
 *   </li>
 *   <li><b>Composition and templates:</b>
 *       <ul>
 *         <li>{@link org.ejavdge.scalar.text.Concat} – concatenates text
 *             fragments, optionally with a separator;</li>
 *         <li>{@link org.ejavdge.scalar.text.Stencil} – substitutes arguments
 *             into a format template using {@link java.lang.String#format};</li>
 *         <li>{@link org.ejavdge.scalar.text.BindOfText} – a monadic bind that
 *             applies a function to the string content and yields a new
 *             {@code Text}.</li>
 *       </ul>
 *   </li>
 *   <li><b>String transformations:</b>
 *       <ul>
 *         <li>{@link org.ejavdge.scalar.text.Trimmed} – removes leading and
 *             trailing whitespace;</li>
 *         <li>{@link org.ejavdge.scalar.text.Lowers} – converts to lowercase
 *             using {@link java.util.Locale#ROOT};</li>
 *         <li>{@link org.ejavdge.scalar.text.WithoutNbsp} – replaces
 *             non-breaking spaces with regular spaces;</li>
 *         <li>{@link org.ejavdge.scalar.text.NonAnsiText} – strips ANSI escape
 *             sequences;</li>
 *         <li>{@link org.ejavdge.scalar.text.PartOfUrl} – URL-encodes its
 *             content;</li>
 *         <li>{@link org.ejavdge.scalar.text.TextFromUrl} – URL-decodes its
 *             content;</li>
 *         <li>{@link org.ejavdge.scalar.text.Match} – extracts a substring
 *             using a regular expression;</li>
 *         <li>{@link org.ejavdge.scalar.text.Uuid} – represents a
 *             {@link java.util.UUID} as text.</li>
 *       </ul>
 *   </li>
 *   <li><b>Adapters from other scalar types:</b>
 *       <ul>
 *         <li>{@link org.ejavdge.scalar.text.TextOfNum} – exposes a
 *             {@link org.ejavdge.scalar.num.Num} as text;</li>
 *         <li>{@link org.ejavdge.scalar.text.Utf8Text} – decodes a
 *             {@link org.ejavdge.scalar.bytes.Bytes} sequence as UTF-8
 *             text.</li>
 *       </ul>
 *   </li>
 *   <li><b>Validation and fallback:</b>
 *       <ul>
 *         <li>{@link org.ejavdge.scalar.text.NonEmpty} – enforces that the
 *             content is not empty;</li>
 *         <li>{@link org.ejavdge.scalar.text.Empty} – a constant empty
 *             string;</li>
 *         <li>{@link org.ejavdge.scalar.text.Fallback} – prefers a primary
 *             value and falls back to a secondary one on failure.</li>
 *       </ul>
 *   </li>
 *   <li><b>Equality and comparison:</b>
 *       <ul>
 *         <li>{@link org.ejavdge.scalar.text.ContentBased} – implements
 *             content-based equality and hashing.</li>
 *       </ul>
 *   </li>
 *   <li><b>Diagnostics and side effects:</b>
 *       <ul>
 *         <li>{@link org.ejavdge.scalar.text.TextAbout} – attaches a
 *             descriptive subject for richer error messages;</li>
 *         <li>{@link org.ejavdge.scalar.text.Notice} – performs an
 *             {@link org.ejavdge.effect.Effect} before returning its text
 *             content.</li>
 *       </ul>
 *   </li>
 * </ul>
 * <p>
 * All operations in this package are lazy: they do not perform any work until
 * {@code content()} is invoked. Combinators can be freely chained to build
 * complex transformations in a declarative, functional style. If an invariant
 * is violated during materialization, an
 * {@link org.ejavdge.error.InvariantViolation} is thrown.
 * </p>
 * <p>
 * A companion package,
 * {@link org.ejavdge.scalar.text.palette}, provides ANSI color wrappers
 * ({@code Green}, {@code Red}) that decorate text for terminal output.
 * </p>
 */
package org.ejavdge.scalar.text;
