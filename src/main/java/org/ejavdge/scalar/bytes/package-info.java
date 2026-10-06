/**
 * Byte sequences and UTF-8 encodings.
 * <p>
 * This package provides the central abstraction for working with binary data
 * throughout the application: the {@link org.ejavdge.scalar.bytes.Bytes}
 * interface. A {@code Bytes} instance represents an immutable sequence of bytes
 * that is materialized on demand via its {@link
 * org.ejavdge.scalar.bytes.Bytes#content()} method. This allows binary data to
 * be composed, transformed, and decorated declaratively, deferring expensive
 * work until the result is actually needed.
 * </p>
 * <p>
 * The package is organized around the {@code Bytes} interface and a set of
 * implementations and decorators that cover common patterns for handling
 * binary data:
 * </p>
 * <ul>
 *   <li><b>Core abstraction:</b>
 *       <ul>
 *         <li>{@link org.ejavdge.scalar.bytes.Bytes} – the functional interface
 *             for an immutable byte sequence, with a nested
 *             {@link org.ejavdge.scalar.bytes.Bytes.Of Bytes.Of}
 *             implementation that wraps a fixed byte array.</li>
 *       </ul>
 *   </li>
 *   <li><b>Encoding and composition:</b>
 *       <ul>
 *         <li>{@link org.ejavdge.scalar.bytes.Utf8} – encodes a
 *             {@link org.ejavdge.scalar.text.Text} as UTF-8 bytes;</li>
 *         <li>{@link org.ejavdge.scalar.bytes.Concat} – concatenates the
 *             contents of several byte sequences into a single array;</li>
 *         <li>{@link org.ejavdge.scalar.bytes.BindOfBytes} – a monadic bind
 *             that applies a function to the byte array and yields a new
 *             {@code Bytes}.</li>
 *       </ul>
 *   </li>
 *   <li><b>Decorators for behavior and diagnostics:</b>
 *       <ul>
 *         <li>{@link org.ejavdge.scalar.bytes.Memo} – memoizes the content to
 *             avoid repeated evaluation;</li>
 *         <li>{@link org.ejavdge.scalar.bytes.WithRetries} – retries obtaining
 *             the content on failure;</li>
 *         <li>{@link org.ejavdge.scalar.bytes.WithTimeout} – bounds the time
 *             spent obtaining the content;</li>
 *         <li>{@link org.ejavdge.scalar.bytes.Verbose} – logs a message before
 *             materializing the content;</li>
 *         <li>{@link org.ejavdge.scalar.bytes.BytesAbout} – attaches a
 *             descriptive subject for richer error messages.</li>
 *       </ul>
 *   </li>
 *   <li><b>Validation and numeric views:</b>
 *       <ul>
 *         <li>{@link org.ejavdge.scalar.bytes.NonEmpty} – enforces that the
 *             content is not empty;</li>
 *         <li>{@link org.ejavdge.scalar.bytes.Size} – exposes the size of the
 *             content as a {@link org.ejavdge.scalar.num.Num}.</li>
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
 */
package org.ejavdge.scalar.bytes;
