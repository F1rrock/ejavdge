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
 * A collection of references (links) associated with a problem, extracted from
 * a problem page.
 * <p>
 * This class implements {@link Text} and represents the list of reference links
 * found in the problem description area. The references are extracted from
 * anchor ({@code <a>}) elements located within a specific section of the page,
 * excluding elements with certain CSS classes and tags. The extracted links are
 * then cleaned up: non-breaking spaces are removed, the text is split into
 * lines, each line is trimmed, and the lines are joined with newlines.
 * <p>
 * The extraction process targets the {@code <div>} with
 * {@code id="probNavTaskArea-ins"}, considering all descendant elements up to
 * (but not including) the element with {@code id="ej-submit-tabs"}. Within that
 * region, only anchor tags are considered, and any element with CSS class
 * {@code "line-table-wb"} or tags {@code <br>} and {@code <style>} are excluded.
 * <p>
 * A {@code ProbRefs} can also be created by wrapping an existing {@link Text}.
 */
public final class ProbRefs implements Text {

    /**
     * The underlying textual content of the problem references.
     */
    private final Text origin;

    /**
     * Creates a problem references text by extracting links from the given
     * problem page using the provided XML engine.
     * <p>
     * The extraction follows these steps:
     * <ol>
     *   <li>Select the {@code <div>} element with
     *       {@code id="probNavTaskArea-ins"}.</li>
     *   <li>Within it, select all descendant elements up to (but not including)
     *       the element with {@code id="ej-submit-tabs"}.</li>
     *   <li>Exclude elements with the CSS class {@code "line-table-wb"} and any
     *       {@code <br>} or {@code <style>} elements.</li>
     *   <li>From the remaining elements, extract all anchor ({@code <a>}) tags
     *       and their {@code href} attributes.</li>
     *   <li>Remove non-breaking spaces, split the resulting string into lines,
     *       and trim each line.</li>
     *   <li>Join the trimmed lines with newlines.</li>
     * </ol>
     * The final text is labelled as "problem references" and prefixed with a
     * newline for readability.
     *
     * @param e the XML engine used to evaluate the XPath selection
     * @param p the problem page from which references are extracted
     */
    public ProbRefs(final XmlEngine e, final ProblemPage p) {
        this(
            new TextAbout(
                "problem references",
                new Concat(
                    new Text.Of("\n"),
                    new Map<>(
                        Trimmed::new,
                        new Lines(
                            new WithoutNbsp(
                                new XmlSelection(
                                    e, p,
                                    new LinksOnly(
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
                )
            )
        );
    }

    /**
     * Creates a problem references text by wrapping an existing text.
     *
     * @param t the text that will become the problem references content
     */
    public ProbRefs(final Text t) {
        this.origin = t;
    }

    /**
     * Returns the textual content of the problem references.
     *
     * @return the problem references as a newline-separated string
     * @throws InvariantViolation if an invariant is violated while retrieving
     *         the content
     */
    @Override
    public String content() throws InvariantViolation {
        return this.origin.content();
    }
}
