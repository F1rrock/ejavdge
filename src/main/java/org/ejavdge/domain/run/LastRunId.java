package org.ejavdge.domain.run;

import org.ejavdge.contest.ProblemPage;
import org.ejavdge.dom.XmlSelection;
import org.ejavdge.dom.engine.XmlEngine;
import org.ejavdge.dom.path.*;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.scalar.text.NonEmpty;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.scalar.text.TextAbout;
import org.ejavdge.scalar.text.Trimmed;

/**
 * The identifier of the most recent run of a problem, extracted from a problem
 * page.
 * <p>
 * This class implements {@link Text} and represents the numeric identifier of
 * the last run shown in the problem's run table. The identifier is extracted
 * from a {@link ProblemPage} using an {@link XmlEngine} and a chain of
 * {@link org.ejavdge.dom.path.DocPath} selections:
 * <ol>
 *   <li>Locate the table rows on the problem page (via {@code PathToTableRows}).</li>
 *   <li>Select the second row (1-based index).</li>
 *   <li>Within that row, find the first {@code <td>} element with CSS class
 *       {@code "b1"}.</li>
 *   <li>Extract its inner text.</li>
 * </ol>
 * The extracted text is trimmed and required to be non-empty. If no run
 * identifier can be found, the fallback message
 * {@code "There is no reports here."} is used. The resulting text is labelled
 * as "last run id".
 * <p>
 * An instance can also be created by wrapping an existing {@link Text}.
 */
public final class LastRunId implements Text {

    /**
     * The underlying textual content representing the last run identifier.
     */
    private final Text origin;

    /**
     * Creates a last run identifier by extracting it from the given problem
     * page using the provided XML engine.
     * <p>
     * The extraction follows these steps:
     * <ol>
     *   <li>Select the table rows on the problem page.</li>
     *   <li>Take the second row (1-based index).</li>
     *   <li>Within that row, select the first {@code <td>} element with CSS
     *       class {@code "b1"}.</li>
     *   <li>Extract its inner text, trim it, and require it to be non-empty.</li>
     * </ol>
     * If the resulting text is empty, the fallback message
     * {@code "There is no reports here."} is used. The final text is labelled
     * as "last run id".
     *
     * @param e the XML engine used to evaluate the XPath selection
     * @param p the problem page from which the last run identifier is extracted
     */
    public LastRunId(final XmlEngine e, final ProblemPage p) {
        this(
            new TextAbout(
                "last run id",
                new NonEmpty(
                    new Trimmed(
                        new XmlSelection(
                            e, p,
                            new InnerText(
                                new OnlyAt(
                                    new Num.Of(1),
                                    new WithClass(
                                        "b1",
                                        new NestedTag(
                                            "td",
                                            new OnlyAt(
                                                new Num.Of(2),
                                                new PathToTableRows()
                                            )
                                        )
                                    )
                                )
                            )
                        )
                    ),
                    new Text.Of("There is no reports here.")
                )
            )
        );
    }

    /**
     * Creates a last run identifier by wrapping an existing text.
     *
     * @param t the text that will become the last run identifier content
     */
    public LastRunId(final Text t) {
        this.origin = t;
    }

    /**
     * Returns the textual content of the last run identifier.
     *
     * @return the last run identifier as a string
     * @throws InvariantViolation if an invariant is violated while retrieving
     *         the content
     */
    @Override
    public String content() throws InvariantViolation {
        return this.origin.content();
    }
}
