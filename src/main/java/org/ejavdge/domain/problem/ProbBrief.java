package org.ejavdge.domain.problem;

import org.ejavdge.contest.ProblemPage;
import org.ejavdge.dom.XmlSelection;
import org.ejavdge.dom.engine.XmlEngine;
import org.ejavdge.dom.path.*;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.items.Items;
import org.ejavdge.items.Lines;
import org.ejavdge.items.Map;
import org.ejavdge.scalar.text.*;

/**
 * A brief description of a problem, extracted from a problem page.
 * <p>
 * This class implements {@link Text} and represents the textual brief of a
 * problem as displayed on the contest system. The brief is obtained from the
 * problem page by extracting the inner text of a specific section, cleaning it
 * up (removing line breaks, style elements, and elements with a particular CSS
 * class), splitting it into lines, trimming each line, and joining them with
 * newlines. The result is guaranteed to be non-empty; if the extraction yields
 * nothing, a fallback message "There is no such problem." is used.
 * <p>
 * A {@code ProbBrief} can also be created by wrapping an existing {@link Text}.
 */
public final class ProbBrief implements Text {

    /**
     * The underlying textual content of the problem brief.
     */
    private final Text origin;

    /**
     * Creates a problem brief by extracting it from the given problem page
     * using the provided XML engine.
     * <p>
     * The extraction follows these steps:
     * <ol>
     *   <li>Select the {@code <div>} element with
     *       {@code id="probNavTaskArea-ins"}.</li>
     *   <li>Within it, select all descendant elements up to (but not including)
     *       the element with {@code id="ej-submit-tabs"}.</li>
     *   <li>Exclude elements with the CSS class {@code "line-table-wb"} and
     *       any {@code <br>} or {@code <style>} elements.</li>
     *   <li>Extract the inner text of the remaining elements.</li>
     *   <li>Remove non-breaking spaces, split into lines, and trim each
     *       line.</li>
     *   <li>Join the trimmed lines with newlines.</li>
     * </ol>
     * If the resulting text is empty, the fallback message
     * "There is no such problem." is used. The final text is labelled as
     * "problem description".
     *
     * @param e the XML engine used to evaluate the XPath selection
     * @param p the problem page from which the brief is extracted
     */
    public ProbBrief(final XmlEngine e, final ProblemPage p) {
        this(
            new TextAbout(
                "problem description",
                new NonEmpty(
                    new Concat(
                        new Text.Of("\n"),
                        new Map<>(
                            Trimmed::new,
                            new Lines(
                                new WithoutNbsp(
                                    new XmlSelection(
                                        e, p,
                                        new InnerText(
                                            new WithoutClass(
                                                "line-table-wb",
                                                new WithoutTags(
                                                    new Items.Of<>(
                                                        new Text.Of("br"),
                                                        new Text.Of("style")
                                                    ),
                                                    new BeforeId(
                                                        "ej-submit-tabs",
                                                        new ChildrenOf(
                                                            new WithId(
                                                                "probNavTaskArea-ins",
                                                                new OnlyTag("div")
                                                            )
                                                        )
                                                    )
                                                )
                                            )
                                        )
                                    )
                                )
                            )
                        )
                    ),
                    new Text.Of("There is no such problem.")
                )
            )
        );
    }

    /**
     * Creates a problem brief by wrapping an existing text.
     *
     * @param t the text that will become the problem brief content
     */
    public ProbBrief(final Text t) {
        this.origin = t;
    }

    /**
     * Returns the textual content of the problem brief.
     *
     * @return the problem brief as a string
     * @throws InvariantViolation if an invariant is violated while retrieving
     *         the content
     */
    @Override
    public String content() throws InvariantViolation {
        return this.origin.content();
    }
}
