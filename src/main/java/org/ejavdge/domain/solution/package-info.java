/**
 * Solution sources, languages, and sample testing.
 * <p>
 * This package contains domain classes that deal with solutions to contest
 * problems: extracting metadata from solution source files, resolving the
 * language to use for submission, submitting the solution to the contest
 * system, and testing the solution against sample fixtures before submission.
 * </p>
 * <p>
 * The main classes in this package are:
 * </p>
 * <ul>
 *   <li>{@link org.ejavdge.domain.solution.LangIndexOf} – extracts the language
 *       index from a marker comment such as {@code // language: <INDEX>} in the
 *       solution file;</li>
 *   <li>{@link org.ejavdge.domain.solution.ProbNameOf} – extracts the problem
 *       short name from a marker comment such as {@code // problem: <NAME>} in
 *       the solution file;</li>
 *   <li>{@link org.ejavdge.domain.solution.Solution} – represents a submission
 *       action that sends a solution file to the contest system as a multipart
 *       form POST;</li>
 *   <li>{@link org.ejavdge.domain.solution.SolutionLang} – resolves the
 *       language identifier for a submission, preferring the problem's preset
 *       language and falling back to the language index from the solution
 *       file;</li>
 *   <li>{@link org.ejavdge.domain.solution.VerdictBySamples} – a verdict that
 *       runs a program against a collection of example fixtures and checks
 *       whether all outputs match the expected results.</li>
 * </ul>
 * <p>
 * These classes rely on contest forms and pages
 * ({@link org.ejavdge.contest.ContestForm},
 * {@link org.ejavdge.contest.ProblemPage}), an XML engine
 * ({@link org.ejavdge.dom.engine.XmlEngine}) for extracting data from pages,
 * and scalar abstractions for numbers and text
 * ({@link org.ejavdge.scalar.num.Num},
 * {@link org.ejavdge.scalar.text.Text}). They also interact with problem domain
 * classes such as {@link org.ejavdge.domain.problem.PresetLang} and
 * {@link org.ejavdge.domain.problem.LangByIndex}.
 * </p>
 */
package org.ejavdge.domain.solution;
