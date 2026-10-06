/**
 * Run reports and judging feedback.
 * <p>
 * This package contains domain classes that represent run reports in the ejudge
 * contest system and provide judging feedback. It covers the retrieval,
 * assembly, and interpretation of report data, as well as effects that wait for
 * or affirm the outcome of a run.
 * </p>
 * <p>
 * The main abstractions in this package include:
 * </p>
 * <ul>
 *   <li><b>Report assembly:</b>
 *       <ul>
 *         <li>{@link org.ejavdge.domain.report.EntireReport} – a complete
 *             textual report assembled from the header, table, and details
 *             sections;</li>
 *         <li>{@link org.ejavdge.domain.report.ReportHeader} – the header
 *             section of a report;</li>
 *         <li>{@link org.ejavdge.domain.report.ReportTable} – the result table
 *             section of a report, with each line prefixed by
 *             {@code "Result: "};</li>
 *         <li>{@link org.ejavdge.domain.report.ReportDetails} – the detailed
 *             section of a report (e.g., compiler output).</li>
 *       </ul>
 *   </li>
 *   <li><b>Convenience reports:</b>
 *       <ul>
 *         <li>{@link org.ejavdge.domain.report.LastReport} – a report for the
 *             most recent run of a problem, derived from a submitted solution
 *             file.</li>
 *       </ul>
 *   </li>
 *   <li><b>Verdicts and readiness:</b>
 *       <ul>
 *         <li>{@link org.ejavdge.domain.report.ReportReadiness} – a verdict
 *             that checks whether a run's report is ready for retrieval based
 *             on its JSON status.</li>
 *       </ul>
 *   </li>
 *   <li><b>Effects:</b>
 *       <ul>
 *         <li>{@link org.ejavdge.domain.report.AffirmingOf} – an effect that
 *             fails if a given verdict is not successful, using a provided
 *             message;</li>
 *         <li>{@link org.ejavdge.domain.report.AwaitingOf} – an effect that
 *             polls a verdict until it becomes successful, with a configurable
 *             delay between attempts.</li>
 *       </ul>
 *   </li>
 *   <li><b>Feedback:</b>
 *       <ul>
 *         <li>{@link org.ejavdge.domain.report.Feedback} – a conditional text
 *             that returns one message if a verdict is successful and another
 *             if it is not.</li>
 *       </ul>
 *   </li>
 * </ul>
 * <p>
 * Most classes in this package rely on an
 * {@link org.ejavdge.dom.engine.XmlEngine} to extract data from report pages
 * and on {@link org.ejavdge.contest.ReportPage} as the source of the report
 * content. The {@link org.ejavdge.domain.Verdict} interface is used to
 * represent success or failure of a run and drives several of the classes here.
 * </p>
 */
package org.ejavdge.domain.report;
