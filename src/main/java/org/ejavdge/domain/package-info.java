/**
 * Core contest domain types.
 * <p>
 * This package contains the fundamental domain abstractions used throughout
 * the ejudge client application. These types represent the essential concepts
 * of contest problems, solutions, runs, and their outcomes, independent of any
 * particular contest system's HTML structure or transport mechanism.
 * </p>
 * <p>
 * The main abstractions in this package are:
 * </p>
 * <ul>
 *   <li>{@link org.ejavdge.domain.Fixture} – a single test case for a problem,
 *       consisting of input data and the expected output data;</li>
 *   <li>{@link org.ejavdge.domain.Verdict} – a functional interface representing
 *       the outcome of an operation that can either succeed or fail.</li>
 * </ul>
 * <p>
 * These types are used by higher-level domain packages such as
 * {@link org.ejavdge.domain.problem}, {@link org.ejavdge.domain.solution},
 * {@link org.ejavdge.domain.run}, and {@link org.ejavdge.domain.report} to
 * model problem statements, solution submissions, run results, and judging
 * feedback.
 * </p>
 */
package org.ejavdge.domain;
