package org.ejavdge.domain.report;

import org.ejavdge.contest.ReportPage;
import org.ejavdge.dom.engine.XmlEngine;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.items.Items;
import org.ejavdge.scalar.text.*;

/**
 * A complete textual representation of a run report in the ejudge contest
 * system.
 * <p>
 * This class implements {@link Text} and assembles the full report from three
 * sections extracted from a {@link ReportPage}:
 * <ol>
 *   <li>the report header (see {@link ReportHeader});</li>
 *   <li>the report table with test results (see {@link ReportTable});</li>
 *   <li>the detailed report information (see {@link ReportDetails}).</li>
 * </ol>
 * The sections are concatenated with newlines separating them, and the final
 * result is trimmed. The assembled text is labelled as "problem's report".
 * <p>
 * The extraction of each section is performed by the corresponding domain class
 * using the same {@link XmlEngine}, but each receives a fresh {@link ReportPage}
 * built from the already fetched page content, avoiding repeated network
 * requests for the same report.
 * <p>
 * An {@code EntireReport} can also be created by wrapping an existing
 * {@link Text}.
 */
public final class EntireReport implements Text {

    /**
     * The underlying textual content of the entire report.
     */
    private final Text origin;

    /**
     * Creates an entire report by extracting its sections from the given report
     * page using the provided XML engine.
     * <p>
     * The report page content is fetched once and then reused to build three
     * sub-pages for the header, table, and details sections. The sections are
     * concatenated with newlines and the result is trimmed. The final text is
     * labelled as "problem's report".
     *
     * @param e the XML engine used to evaluate XPath selections
     * @param p the report page from which the report is extracted
     */
    public EntireReport(final XmlEngine e, final ReportPage p) {
        this(
            new TextAbout(
                "problem's report",
                new Trimmed(
                    new BindOfText(
                        p,
                        page -> new Concat(
                            new Text.Of("\n"),
                            new Items.Of<>(
                                new ReportHeader(
                                    e,
                                    new ReportPage(new Text.Of(page))
                                ),
                                new ReportTable(
                                    e,
                                    new ReportPage(new Text.Of(page))
                                ),
                                new ReportDetails(
                                    e,
                                    new ReportPage(new Text.Of(page))
                                )
                            )
                        )
                    )
                )
            )
        );
    }

    /**
     * Creates an entire report by wrapping an existing text.
     *
     * @param t the text that will become the report content
     */
    public EntireReport(final Text t) {
        this.origin = t;
    }

    /**
     * Returns the textual content of the entire report.
     *
     * @return the full report as a string
     * @throws InvariantViolation if an invariant is violated while retrieving
     *         the content
     */
    @Override
    public String content() throws InvariantViolation {
        return this.origin.content();
    }
}
