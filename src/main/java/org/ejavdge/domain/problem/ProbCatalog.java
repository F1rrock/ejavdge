package org.ejavdge.domain.problem;

import org.ejavdge.contest.MainPage;
import org.ejavdge.dom.XmlSelection;
import org.ejavdge.dom.engine.XmlEngine;
import org.ejavdge.dom.path.*;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.NonEmpty;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.scalar.text.TextAbout;

/**
 * A catalog of available problems on the contest's main page.
 * <p>
 * This class implements {@link Text} and represents a comma-separated list of
 * problem names as displayed in the contest navigation. The catalog is
 * extracted from the main page using an {@link XmlEngine} and a chain of
 * {@link org.ejavdge.dom.path.DocPath} selections:
 * <ol>
 *   <li>Locate a {@code <tr>} element with {@code id="probNavTopList"}.</li>
 *   <li>Within it, find a {@code <ul>} with class {@code "nTopNavList"}.</li>
 *   <li>Within that, find all {@code <a>} elements with class {@code "tab"}.</li>
 *   <li>Extract the inner text of those links and join them with
 *       {@code ", "}.</li>
 * </ol>
 * The result is guaranteed to be non-empty via {@link NonEmpty}; if no problems
 * are found, the fallback message "There is no available problems." is used.
 * The final text is labelled as "problem catalog".
 * <p>
 * A {@code ProbCatalog} can also be created by wrapping an existing
 * {@link Text}.
 */
public final class ProbCatalog implements Text {

    /**
     * The underlying textual content of the problem catalog.
     */
    private final Text origin;

    /**
     * Creates a problem catalog by extracting it from the given main page using
     * the provided XML engine.
     * <p>
     * The extraction follows these steps:
     * <ol>
     *   <li>Select the {@code <tr>} element with {@code id="probNavTopList"}.</li>
     *   <li>Within it, select the {@code <ul>} with class
     *       {@code "nTopNavList"}.</li>
     *   <li>Within that, select all {@code <a>} elements with class
     *       {@code "tab"}.</li>
     *   <li>Extract the inner text of those anchor elements, joining them with
     *       {@code ", "}.</li>
     * </ol>
     * If the resulting text is empty, the fallback message
     * "There is no available problems." is used. The final text is labelled as
     * "problem catalog".
     *
     * @param e the XML engine used to evaluate the XPath selection
     * @param p the main page from which the problem catalog is extracted
     */
    public ProbCatalog(final XmlEngine e, final MainPage p) {
        this(
            new TextAbout(
                "problem catalog",
                new NonEmpty(
                    new XmlSelection(
                        e, p,
                        new InnerText(
                            new Text.Of(", "),
                            new WithClass(
                                "tab",
                                new NestedTag(
                                    "a",
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
                    ),
                    new Text.Of("There is no available problems.")
                )
            )
        );
    }

    /**
     * Creates a problem catalog by wrapping an existing text.
     *
     * @param t the text that will become the problem catalog content
     */
    public ProbCatalog(final Text t) {
        this.origin = t;
    }

    /**
     * Returns the textual content of the problem catalog.
     *
     * @return the problem catalog as a string
     * @throws InvariantViolation if an invariant is violated while retrieving
     *         the content
     */
    @Override
    public String content() throws InvariantViolation {
        return this.origin.content();
    }
}
