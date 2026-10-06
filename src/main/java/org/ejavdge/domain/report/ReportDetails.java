package org.ejavdge.domain.report;

import org.ejavdge.contest.ReportPage;
import org.ejavdge.dom.XmlSelection;
import org.ejavdge.dom.engine.XmlEngine;
import org.ejavdge.dom.path.*;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.items.Items;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.scalar.text.TextAbout;

/**
 * The detailed section of a run report in the ejudge contest system.
 * <p>
 * This class implements {@link Text} and extracts the detailed textual
 * information from a {@link ReportPage}. The details typically include the
 * compiler output, error messages, or other verbose information associated
 * with a run.
 * <p>
 * The extraction is performed using an {@link XmlEngine} and a chain of
 * {@link org.ejavdge.dom.path.DocPath} selections:
 * <ol>
 *   <li>Locate a {@code <div>} element with CSS class {@code "l14"}.</li>
 *   <li>Within it, select all descendant elements, excluding {@code <br>}
 *       tags.</li>
 *   <li>Among those, find the last element that carries both CSS classes
 *       {@code "table"} and {@code "b1"}.</li>
 *   <li>Extract the inner text of that element.</li>
 * </ol>
 * The resulting text is labelled as "details of problem's report".
 * <p>
 * A {@code ReportDetails} can also be created by wrapping an existing
 * {@link Text}.
 */
public final class ReportDetails implements Text {

    /**
     * The underlying textual content of the report details.
     */
    private final Text origin;

    /**
     * Creates a report details text by extracting it from the given report page
     * using the provided XML engine.
     * <p>
     * The extraction follows these steps:
     * <ol>
     *   <li>Select the {@code <div>} element with class {@code "l14"}.</li>
     *   <li>Within it, select all descendant elements, excluding {@code <br>}
     *       tags.</li>
     *   <li>Among those, find the last element that carries both classes
     *       {@code "table"} and {@code "b1"}.</li>
     *   <li>Extract the inner text of that element.</li>
     * </ol>
     * The final text is labelled as "details of problem's report".
     *
     * @param e the XML engine used to evaluate the XPath selection
     * @param p the report page from which the details are extracted
     */
    public ReportDetails(final XmlEngine e, final ReportPage p) {
        this(
            new TextAbout(
                "details of problem's report",
                new XmlSelection(
                    e, p,
                    new InnerText(
                        new AfterClasses(
                            new Items.Of<>(
                                new Text.Of("table"),
                                new Text.Of("b1")
                            ),
                            new WithoutTag(
                                "br",
                                new ChildrenOf(
                                    new WithClass(
                                        "l14",
                                        new OnlyTag("div")
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
     * Creates a report details text by wrapping an existing text.
     *
     * @param t the text that will become the report details content
     */
    public ReportDetails(final Text t) {
        this.origin = t;
    }

    /**
     * Returns the textual content of the report details.
     *
     * @return the report details as a string
     * @throws InvariantViolation if an invariant is violated while retrieving
     *         the content
     */
    @Override
    public String content() throws InvariantViolation {
        return this.origin.content();
    }
}
