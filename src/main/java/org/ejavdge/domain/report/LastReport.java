package org.ejavdge.domain.report;

import org.ejavdge.contest.ContestResource;
import org.ejavdge.contest.MainPage;
import org.ejavdge.contest.ProblemPage;
import org.ejavdge.contest.ReportPage;
import org.ejavdge.dom.engine.XmlEngine;
import org.ejavdge.domain.problem.ProbByName;
import org.ejavdge.domain.run.LastRunId;
import org.ejavdge.domain.run.LastRunInfo;
import org.ejavdge.domain.solution.ProbNameOf;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.file.ByteFile;
import org.ejavdge.scalar.text.*;
import org.ejavdge.web.context.ProbId;
import org.ejavdge.web.context.RunId;

/**
 * A textual report about the most recent run of a problem, identified by a
 * submitted solution file.
 * <p>
 * This class implements {@link Text} and builds a report for the last
 * submission of the problem associated with the given solution file. The
 * report is obtained through a sequence of contest interactions:
 * <ol>
 *   <li>Resolve the problem name from the solution file and look up its
 *       identifier on the main page.</li>
 *   <li>Fetch the problem page using the resolved problem id.</li>
 *   <li>Determine the identifier of the last run for that problem.</li>
 *   <li>Fetch the report page for that run and assemble the full report
 *       using {@link EntireReport}.</li>
 * </ol>
 * If no report is available for the last run, the class falls back to the
 * last run information obtained from the problem page. If that information is
 * also unavailable, a generic message is produced indicating that no
 * information about the last run is available.
 * <p>
 * An instance can also be created by wrapping an existing {@link Text}.
 */
public final class LastReport implements Text {

    /**
     * The underlying textual content of the report.
     */
    private final Text origin;

    /**
     * Creates a report for the last run of the problem associated with the
     * given solution file.
     * <p>
     * The report is assembled as follows:
     * <ol>
     *   <li>The problem name is derived from the solution file using
     *       {@link ProbNameOf} and resolved to a problem id via
     *       {@link ProbByName} on the main page.</li>
     *   <li>The problem page is fetched using the resolved problem id.</li>
     *   <li>The identifier of the last run is obtained from the problem page
     *       via {@link LastRunId}.</li>
     *   <li>The report page for that run is fetched and converted into an
     *       {@link EntireReport}.</li>
     *   <li>If the resulting report is empty, a fallback is used: the last run
     *       information from the problem page is retrieved via
     *       {@link LastRunInfo}. If that too is empty, the text
     *       "There is no available info about last run of current problem" is
     *       produced.</li>
     * </ol>
     *
     * @param f the solution file from which the problem name is derived
     * @param e the XML engine used to evaluate XPath selections
     * @param r the contest resource providing access to the contest system
     */
    public LastReport(final ByteFile f, final XmlEngine e, final ContestResource r) {
        this(
            new BindOfText(
                new ProblemPage(
                    r,
                    new ProbId(
                        new ProbByName(
                            e,
                            new MainPage(r),
                            new ProbNameOf(f)
                        )
                    )
                ),
                page -> new BindOfText(
                    new EntireReport(
                        e,
                        new ReportPage(
                            r,
                            new RunId(
                                new LastRunId(
                                    e,
                                    new ProblemPage(
                                        new Text.Of(page)
                                    )
                                )
                            )
                        )
                    ),
                    report -> new Fallback(
                        new NonEmpty(new Text.Of(report)),
                        new NonEmpty(
                            new LastRunInfo(
                                e,
                                new ProblemPage(
                                    new Text.Of(page)
                                )
                            ),
                            new Concat(
                                "There is no available info about ",
                                "last run of current problem"
                            )
                        )
                    )
                )
            )
        );
    }

    /**
     * Creates a report by wrapping an existing text.
     *
     * @param t the text that will become the report content
     */
    public LastReport(final Text t) {
        this.origin = t;
    }

    /**
     * Returns the textual content of the report.
     *
     * @return the report as a string
     * @throws InvariantViolation if an invariant is violated while retrieving
     *         the content
     */
    @Override
    public String content() throws InvariantViolation {
        return this.origin.content();
    }
}
