/**
 * Numeric values and parsing.
 * <p>
 * This package provides the central abstraction for working with integers
 * throughout the application: the {@link org.ejavdge.scalar.num.Num} interface.
 * A {@code Num} instance represents a numeric value that is materialized on
 * demand via its {@link org.ejavdge.scalar.num.Num#value()} method. This allows
 * numeric values to be composed, validated, and transformed declaratively,
 * deferring expensive work (such as parsing or network access) until the result
 * is actually needed.
 * </p>
 * <p>
 * The package is organized around the {@code Num} interface and a set of
 * implementations and decorators that cover common patterns for handling
 * numeric data:
 * </p>
 * <ul>
 *   <li><b>Core abstraction:</b>
 *       <ul>
 *         <li>{@link org.ejavdge.scalar.num.Num} – the functional interface for
 *             a lazy integer value, with a nested
 *             {@link org.ejavdge.scalar.num.Num.Of Num.Of} implementation that
 *             wraps a fixed integer.</li>
 *       </ul>
 *   </li>
 *   <li><b>Parsing:</b>
 *       <ul>
 *         <li>{@link org.ejavdge.scalar.num.NumOfText} – parses an integer from
 *             a decimal string;</li>
 *         <li>{@link org.ejavdge.scalar.num.NumOfHex} – parses an integer from
 *             a hexadecimal string.</li>
 *       </ul>
 *   </li>
 *   <li><b>Arithmetic and composition:</b>
 *       <ul>
 *         <li>{@link org.ejavdge.scalar.num.SumOf} – the sum of two numeric
 *             values;</li>
 *         <li>{@link org.ejavdge.scalar.num.Fallback} – prefers a primary value
 *             and falls back to a secondary one on failure.</li>
 *       </ul>
 *   </li>
 *   <li><b>Validation:</b>
 *       <ul>
 *         <li>{@link org.ejavdge.scalar.num.NonNegative} – enforces that the
 *             value is greater than or equal to zero;</li>
 *         <li>{@link org.ejavdge.scalar.num.Positive} – enforces that the value
 *             is strictly greater than zero.</li>
 *       </ul>
 *   </li>
 *   <li><b>Diagnostics:</b>
 *       <ul>
 *         <li>{@link org.ejavdge.scalar.num.NumAbout} – attaches a descriptive
 *             subject to a numeric value for richer error messages.</li>
 *       </ul>
 *   </li>
 * </ul>
 * <p>
 * All operations in this package are lazy: they do not perform any work until
 * {@code value()} is invoked. Combinators can be freely chained to build
 * complex numeric expressions in a declarative, functional style. If an
 * invariant is violated during materialization, an
 * {@link org.ejavdge.error.InvariantViolation} is thrown.
 * </p>
 */
package org.ejavdge.scalar.num;
