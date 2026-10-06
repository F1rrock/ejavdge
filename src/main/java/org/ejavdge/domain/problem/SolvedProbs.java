package org.ejavdge.domain.problem;

import org.ejavdge.contest.MainPage;
import org.ejavdge.dom.XmlSelection;
import org.ejavdge.dom.engine.XmlEngine;
import org.ejavdge.dom.path.*;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.scalar.text.TextAbout;

/**
 * A comma-separated list of problems that have been solved by the user, as
 * displayed on the contest's main page.
 * <p>
 * This class implements {@link Text} and represents the names of solved
 * problems extracted from the contest navigation. The extraction is performed
 * on the main page using an {@link XmlEngine} and a chain of
 * {@link org.ejavdge.dom.path.DocPath} selections:
 * <ol>
 *   <li>Locate a {@code <tr>} element with {@code id="probNavTopList"}.</li>
 *   <li>Within it, find a {@code <ul>} with class {@code "nTopNavList"}.</li>
 *   <li>Within that, find a {@code <div>} with class {@code "nProbOk"}.</li>
 *   <li>Within that, find {@code <a>} elements with class {@code "tab"}.</li>
 *   <li>Extract the inner text of those anchor elements, joining them with
 *       {@code ", "}.</li>
 * </ol>
 * The resulting text is labelled as "solved problems".
 * <p>
 * A {@code SolvedProbs} can also be created by wrapping an existing
 * {@link Text}.
 */
public final class SolvedProbs implements Text {

    /**
     * The underlying textual content of the solved problems list.
     */
    private final Text origin;

    /**
     * Creates a solved problems list by extracting it from the given main page
     * using the provided XML engine.
     * <p>
     * The extraction follows these steps:
     * <ol>
     *   <li>Select the {@code <tr>} element with {@code id="probNavTopList"}.</li>
     *   <li>Within it, select the {@code <ul>} with class
     *       {@code "nTopNavList"}.</li>
     *   <li>Within that, select the {@code <div>} with class
     *       {@code "nProbOk"}.</li>
     *   <li>Within that, select all {@code <a>} elements with class
     *       {@code "tab"}.</li>
     *   <li>Extract the inner text of those anchor elements, joining them with
     *       {@code ", "}.</li>
     * </ol>
     * The final text is labelled as "solved problems".
     *
     * @param e the XML engine used to evaluate the XPath selection
     * @param p the main page from which the solved problems are extracted
     */
    public SolvedProbs(final XmlEngine e, final MainPage p) {
        this(
            new TextAbout(
                "solved problems",
                new XmlSelection(
                    e, p,
                    new InnerText(
                        new Text.Of(", "),
                        new WithClass(
                            "tab",
                            new NestedTag(
                                "a",
                                new WithClass(
                                    "nProbOk",
                                    new NestedTag(
                                        "div",
                                        new WithClass(
                                            "nTopNavList",
                                            new NestedTag(
                                                "ul",
                                                new WithId(
                                                    "probNavTopList",
                                                    new OnlyTag("tr")
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
     * Creates a solved problems list by wrapping an existing text.
     *
     * @param t the text that will become the solved problems content
     */
    public SolvedProbs(final Text t) {
        this.origin = t;
    }

    /**
     * Returns the textual content of the solved problems list.
     *
     * @return the solved problems as a comma-separated string
     * @throws InvariantViolation if an invariant is violated while retrieving
     *         the content
     */
    @Override
    public String content() throws InvariantViolation {
        return this.origin.content();
    }
}
