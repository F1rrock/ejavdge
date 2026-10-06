package org.ejavdge.domain.report;

import org.ejavdge.contest.ReportPage;
import org.ejavdge.dom.XmlSelection;
import org.ejavdge.dom.engine.XmlEngine;
import org.ejavdge.dom.path.*;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.items.Map;
import org.ejavdge.items.SignificantLines;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.scalar.text.Concat;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.scalar.text.TextAbout;

/**
 * The table section of a run report in the ejudge contest system.
 * <p>
 * This class implements {@link Text} and extracts the result table from a
 * {@link ReportPage}. The table typically lists the individual test cases and
 * their results. Each significant line of the extracted text is prefixed with
 * {@code "Result: "} to make the outcome of each test explicit in the textual
 * report.
 * <p>
 * The extraction is performed using an {@link XmlEngine} and a chain of
 * {@link org.ejavdge.dom.path.DocPath} selections:
 * <ol>
 *   <li>Locate all {@code <td>} elements with CSS class {@code "b1"}.</li>
 *   <li>Select the second such element (1-based index via
 *       {@link org.ejavdge.dom.path.OnlyAt}).</li>
 *   <li>Extract its inner text, joining individual text nodes with
 *       newlines.</li>
 *   <li>Split the result into significant lines and prefix each with
 *       {@code "Result: "}.</li>
 * </ol>
 * The resulting text is labelled as "table of problem's report" and prefixed
 * with a newline for readability.
 * <p>
 * A {@code ReportTable} can also be created by wrapping an existing
 * {@link Text}.
 */
public final class ReportTable implements Text {

    /**
     * The underlying textual content of the report table.
     */
    private final Text origin;

    /**
     * Creates a report table by extracting it from the given report page using
     * the provided XML engine.
     * <p>
     * The extraction follows these steps:
     * <ol>
     *   <li>Select all {@code <td>} elements with class {@code "b1"}.</li>
     *   <li>Take the second such element.</li>
     *   <li>Extract its inner text, joining text nodes with newlines.</li>
     *   <li>Split the text into significant lines and prefix each line with
     *       {@code "Result: "}.</li>
     * </ol>
     * The final text is labelled as "table of problem's report".
     *
     * @param e the XML engine used to evaluate the XPath selection
     * @param p the report page from which the table is extracted
     */
    public ReportTable(final XmlEngine e, final ReportPage p) {
        this(
            new TextAbout(
                "table of problem's report",
                new Concat(
                    new Text.Of("\n"),
                    new Map<>(
                        l -> new Concat(new Text.Of("Result: "), l),
                        new SignificantLines(
                            new XmlSelection(
                                e, p,
                                new InnerText(
                                    new Text.Of("\n"),
                                    new OnlyAt(
                                        new Num.Of(2),
                                        new WithClass(
                                            "b1",
                                            new OnlyTag("td")
                                        )
                                    )
                                )
                            )
                        )
                    )
                )
            )
        );
    }

    /**
     * Creates a report table by wrapping an existing text.
     *
     * @param t the text that will become the report table content
     */
    public ReportTable(final Text t) {
        this.origin = t;
    }

    /**
     * Returns the textual content of the report table.
     *
     * @return the report table as a string with each line prefixed by
     *         {@code "Result: "}
     * @throws InvariantViolation if an invariant is violated while retrieving
     *         the content
     */
    @Override
    public String content() throws InvariantViolation {
        return this.origin.content();
    }
}
