/**
 * Contest pages and form resources on the ejudge web client.
 * <p>
 * This package provides abstractions for fetching and submitting data to an
 * ejudge contest system. It includes:
 * <ul>
 *   <li>{@link org.ejavdge.contest.ContestResource} – a resource that can be
 *       fetched with an authenticated request;</li>
 *   <li>{@link org.ejavdge.contest.MainPage} – the main page of the contest;</li>
 *   <li>{@link org.ejavdge.contest.ProblemPage} – a page displaying a specific
 *       problem;</li>
 *   <li>{@link org.ejavdge.contest.ReportPage} – a page displaying a run
 *       report;</li>
 *   <li>{@link org.ejavdge.contest.StatusInJson} – the JSON-formatted status of
 *       a run;</li>
 *   <li>{@link org.ejavdge.contest.ContestForm} – a form that can be submitted
 *       to the contest system.</li>
 * </ul>
 * All of these classes rely on an authenticated session and a web driver to
 * communicate with the contest system.
 */
package org.ejavdge.contest;
