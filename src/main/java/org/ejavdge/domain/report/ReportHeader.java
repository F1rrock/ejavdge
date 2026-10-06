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
 * The header section of a run report in the ejudge contest system.
 * <p>
 * This class implements {@link Text} and extracts the header information from
 * a {@link ReportPage}. The header typically contains summary data about the
 * run, such as the submission time, problem name, or compiler information.
 * <p>
 * The extraction is performed using an {@link XmlEngine} and a chain of
 * {@link org.ejavdge.dom.path.DocPath} selections:
 * <ol>
 *   <li>Locate a {@code <div>} element with CSS class {@code "l14"}.</li>
 *   <li>Within it, select all descendant elements, excluding {@code <br>} and
 *       {@code <p>} tags.</li>
 *   <li>Among those, consider only the elements that appear before the first
 *       {@code <table>} element.</li>
 *   <li>Extract the inner text of the selected elements.</li>
 * </ol>
 * The resulting text is labelled as "header of problem's report".
 * <p>
 * A {@code ReportHeader} can also be created by wrapping an existing
 * {@link Text}.
 */
public final class ReportHeader implements Text {

    /**
     * The underlying textual content of the report header.
     */
    private final Text origin;

    /**
     * Creates a report header by extracting it from the given report page using
     * the provided XML engine.
     * <p>
     * The extraction follows these steps:
     * <ol>
     *   <li>Select the {@code <div>} element with class {@code "l14"}.</li>
     *   <li>Within it, select all descendant elements, excluding {@code <br>}
     *       and {@code <p>} tags.</li>
     *   <li>Among those, consider only the elements that precede the first
     *       {@code <table>} element.</li>
     *   <li>Extract the inner text of the selected elements.</li>
     * </ol>
     * The final text is labelled as "header of problem's report".
     *
     * @param e the XML engine used to evaluate the XPath selection
     * @param p the report page from which the header is extracted
     */
    public ReportHeader(final XmlEngine e, final ReportPage p) {
        this(
            new TextAbout(
                "header of problem's report",
                new XmlSelection(
                    e, p,
                    new InnerText(
                        new BeforeTag(
                            "table",
                            new WithoutTags(
                                new Items.Of<>(
                                    new Text.Of("br"),
                                    new Text.Of("p")
                                ),
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
     * Creates a report header by wrapping an existing text.
     *
     * @param t the text that will become the report header content
     */
    public ReportHeader(final Text t) {
        this.origin = t;
    }

    /**
     * Returns the textual content of the report header.
     *
     * @return the report header as a string
     * @throws InvariantViolation if an invariant is violated while retrieving
     *         the content
     */
    @Override
    public String content() throws InvariantViolation {
        return this.origin.content();
    }
}
