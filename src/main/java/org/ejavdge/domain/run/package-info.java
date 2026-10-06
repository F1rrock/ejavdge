/**
 * Run identifiers and probe results.
 * <p>
 * This package contains domain classes that deal with runs (submissions) in the
 * ejudge contest system, focusing on identifying the most recent run and on
 * probing a solution against the example tests of a problem.
 * </p>
 * <p>
 * The main classes in this package are:
 * </p>
 * <ul>
 *   <li>{@link org.ejavdge.domain.run.LastRunId} – extracts the numeric
 *       identifier of the last run from a problem page;</li>
 *   <li>{@link org.ejavdge.domain.run.LastRunInfo} – builds a textual summary
 *       of the last run, pairing column labels with their values;</li>
 *   <li>{@link org.ejavdge.domain.run.PathToTableRows} – a document path that
 *       selects the rows of the run table on a problem page;</li>
 *   <li>{@link org.ejavdge.domain.run.VerdictOfProbe} – a verdict that runs a
 *       Java program against the example tests of a problem, used for probing
 *       a solution before submission.</li>
 * </ul>
 * <p>
 * These classes rely on contest pages (such as
 * {@link org.ejavdge.contest.ProblemPage}) and an
 * {@link org.ejavdge.dom.engine.XmlEngine} to extract data from the contest
 * system, and they use abstractions from
 * {@link org.ejavdge.domain.problem} and
 * {@link org.ejavdge.domain.solution} to interpret problems and solutions.
 * </p>
 */
package org.ejavdge.domain.run;
