package org.ejavdge.domain.problem;

import org.ejavdge.contest.MainPage;
import org.ejavdge.dom.XmlSelection;
import org.ejavdge.dom.engine.XmlEngine;
import org.ejavdge.dom.path.*;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.scalar.num.NumAbout;
import org.ejavdge.scalar.num.NumOfText;
import org.ejavdge.scalar.text.Match;
import org.ejavdge.scalar.text.Text;

/**
 * A numeric value representing the identifier of a problem, looked up by its
 * name on the contest's main page.
 * <p>
 * This class implements {@link Num} and resolves a problem name to its numeric
 * {@code prob_id} by parsing the contest's navigation links. The extraction is
 * performed on the main page using an {@link XmlEngine} and a chain of
 * {@link org.ejavdge.dom.path.DocPath} selections:
 * <ol>
 *   <li>Locate a {@code <tr>} element with {@code id="probNavTopList"}.</li>
 *   <li>Within it, find a {@code <ul>} with class {@code "nTopNavList"}.</li>
 *   <li>Within that, find an {@code <a>} element with class {@code "tab"}.</li>
 *   <li>Among those links, select the one whose exact text matches the given
 *       problem name.</li>
 *   <li>Extract its {@code href} attribute and parse out the numeric
 *       {@code prob_id} value using a regular expression.</li>
 * </ol>
 * The resulting number is labelled as "problem's id" via {@link NumAbout} and
 * converted from text using {@link NumOfText}. If no matching problem is found,
 * the extraction fails with the message "There is no problem with this name."
 * <p>
 * A {@code ProbByName} can also be created by wrapping an existing {@link Num},
 * in which case it simply delegates to that number.
 */
public final class ProbByName implements Num {

    /**
     * The underlying numeric value.
     */
    private final Num origin;

    /**
     * Creates a problem identifier by resolving the given problem name on the
     * main page.
     * <p>
     * The name is matched against the text of navigation links inside the
     * problem list. The corresponding link's {@code href} is then parsed with
     * the regular expression {@code (?<=prob_id=\s*)\d+} to extract the numeric
     * problem id.
     *
     * @param e the XML engine used to evaluate the XPath selection
     * @param p the main page on which the problem list is located
     * @param t the problem name to look up
     */
    public ProbByName(final XmlEngine e, final MainPage p, final Text t) {
        this(
            new NumAbout(
                "problem's id",
                new NumOfText(
                    new Match(
                        new XmlSelection(
                            e, p,
                            new LinksOnly(
                                new WithText(
                                    t,
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
                            )
                        ),
                        new Text.Of("(?<=prob_id=\\s*)\\d+"),
                        new Text.Of("There is no problem with this name.")
                    )
                )
            )
        );
    }

    /**
     * Creates a problem identifier by wrapping an existing numeric value.
     *
     * @param n the numeric value to wrap
     */
    public ProbByName(final Num n) {
        this.origin = n;
    }

    /**
     * Returns the numeric value of this problem identifier.
     *
     * @return the problem id as an integer
     * @throws InvariantViolation if an invariant is violated while retrieving
     *         the value, or if no matching problem is found
     */
    @Override
    public int value() throws InvariantViolation {
        return this.origin.value();
    }
}
