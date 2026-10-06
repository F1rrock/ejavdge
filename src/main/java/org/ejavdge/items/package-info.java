/**
 * Lazy sequences and collection transforms.
 * <p>
 * This package provides the central abstraction for working with collections
 * throughout the application: the {@link org.ejavdge.items.Items} interface. An
 * {@code Items} instance represents a lazy sequence of elements that is
 * materialized on demand via its {@link org.ejavdge.items.Items#contents()}
 * method. This allows collections to be composed, transformed, and filtered
 * declaratively, deferring expensive work (such as network requests or parsing)
 * until the result is actually needed.
 * </p>
 * <p>
 * The package is organized around a small set of core interfaces and a rich set
 * of combinators:
 * </p>
 * <ul>
 *   <li><b>Core abstraction:</b>
 *       <ul>
 *         <li>{@link org.ejavdge.items.Items} – the functional interface for a
 *             lazy sequence, with a nested
 *             {@link org.ejavdge.items.Items.Of Items.Of} implementation that
 *             wraps a fixed list of elements.</li>
 *       </ul>
 *   </li>
 *   <li><b>Transformation and composition:</b>
 *       <ul>
 *         <li>{@link org.ejavdge.items.Map} – applies a function to each
 *             element, producing a new collection;</li>
 *         <li>{@link org.ejavdge.items.BindOfItems} – a monadic bind that
 *             applies a function to the whole list and yields a new
 *             collection;</li>
 *         <li>{@link org.ejavdge.items.Joint} – concatenates the contents of
 *             several collections into a single flat list;</li>
 *         <li>{@link org.ejavdge.items.ZipOf} – transposes a collection of
 *             collections, grouping elements that share the same position.</li>
 *       </ul>
 *   </li>
 *   <li><b>Filtering and slicing:</b>
 *       <ul>
 *         <li>{@link org.ejavdge.items.OnlyWhere} – retains elements satisfying
 *             a predicate;</li>
 *         <li>{@link org.ejavdge.items.OnlyUntil} – retains elements before the
 *             first element matching a predicate;</li>
 *         <li>{@link org.ejavdge.items.OnlyAfter} – retains elements after the
 *             first element matching a predicate;</li>
 *         <li>{@link org.ejavdge.items.OnlyFirst} – retains a fixed number of
 *             leading elements;</li>
 *         <li>{@link org.ejavdge.items.WithoutFirst} – skips a fixed number of
 *             leading elements;</li>
 *         <li>{@link org.ejavdge.items.Split} – splits a collection into groups
 *             separated by elements matching a predicate.</li>
 *       </ul>
 *   </li>
 *   <li><b>Text-oriented helpers:</b>
 *       <ul>
 *         <li>{@link org.ejavdge.items.Lines} – exposes a {@link org.ejavdge.scalar.text.Text} as a
 *             collection of line fragments;</li>
 *         <li>{@link org.ejavdge.items.SignificantLines} – exposes only the
 *             non-empty, trimmed lines of a text.</li>
 *       </ul>
 *   </li>
 *   <li><b>Validation and diagnostics:</b>
 *       <ul>
 *         <li>{@link org.ejavdge.items.Populated} – enforces that a collection
 *             is non-empty;</li>
 *         <li>{@link org.ejavdge.items.ItemsAbout} – attaches a descriptive
 *             subject to a collection for richer error messages.</li>
 *       </ul>
 *   </li>
 * </ul>
 * <p>
 * All operations in this package are lazy: they do not perform any work until
 * {@code contents()} is invoked. Combinators can be freely chained to build
 * complex transformations in a declarative, functional style. If an invariant
 * is violated during materialization, an
 * {@link org.ejavdge.error.InvariantViolation} is thrown.
 * </p>
 */
package org.ejavdge.items;
