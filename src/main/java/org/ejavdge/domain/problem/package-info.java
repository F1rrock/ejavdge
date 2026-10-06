/**
 * Problem catalog, statements, and attachments.
 * <p>
 * This package contains domain classes that represent problems in the ejudge
 * contest system, covering their metadata, content, and associated resources.
 * These classes typically extract data from contest pages (such as the main
 * page or a specific problem page) using an
 * {@link org.ejavdge.dom.engine.XmlEngine} and composable XPath expressions
 * from {@link org.ejavdge.dom.path}. The extracted information is then exposed
 * through standard scalar interfaces like
 * {@link org.ejavdge.scalar.text.Text},
 * {@link org.ejavdge.scalar.num.Num}, or
 * {@link org.ejavdge.items.Items}.
 * </p>
 * <p>
 * The package includes the following categories of classes:
 * </p>
 * <ul>
 *   <li><b>Catalog and navigation:</b>
 *       <ul>
 *         <li>{@link org.ejavdge.domain.problem.ProbCatalog} – a comma-separated
 *             list of available problems on the main page;</li>
 *         <li>{@link org.ejavdge.domain.problem.SolvedProbs} – a list of problems
 *             already solved by the user;</li>
 *         <li>{@link org.ejavdge.domain.problem.ProbByName} – resolves a problem
 *             name to its numeric identifier.</li>
 *       </ul>
 *   </li>
 *   <li><b>Problem statement content:</b>
 *       <ul>
 *         <li>{@link org.ejavdge.domain.problem.ProbBrief} – a cleaned-up textual
 *             brief (description) of a problem;</li>
 *         <li>{@link org.ejavdge.domain.problem.ProbExamples} – a collection of
 *             input/output example fixtures parsed from the brief;</li>
 *         <li>{@link org.ejavdge.domain.problem.ProbRefs} – reference links found
 *             in the problem description.</li>
 *       </ul>
 *   </li>
 *   <li><b>Attachments and language selection:</b>
 *       <ul>
 *         <li>{@link org.ejavdge.domain.problem.ProbAttachments} – file references
 *             (attachments) associated with a problem;</li>
 *         <li>{@link org.ejavdge.domain.problem.LangByIndex} – a problem's
 *             language identifier selected by index;</li>
 *         <li>{@link org.ejavdge.domain.problem.PresetLang} – the preset language
 *             identifier of a problem.</li>
 *       </ul>
 *   </li>
 * </ul>
 * <p>
 * Together, these classes provide a high-level, domain-oriented view of problem
 * data, decoupling the rest of the application from the specifics of the
 * contest system's HTML structure.
 * </p>
 */
package org.ejavdge.domain.problem;
