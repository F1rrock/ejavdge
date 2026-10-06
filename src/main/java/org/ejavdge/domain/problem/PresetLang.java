package org.ejavdge.domain.problem;

import org.ejavdge.contest.ProblemPage;
import org.ejavdge.dom.XmlSelection;
import org.ejavdge.dom.engine.XmlEngine;
import org.ejavdge.dom.path.*;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.scalar.num.NumAbout;
import org.ejavdge.scalar.num.NumOfText;
import org.ejavdge.scalar.text.NonEmpty;

/**
 * A numeric value representing the preset language identifier of a problem.
 * <p>
 * This class implements {@link Num} and extracts the preset language id from a
 * problem page. The preset language is determined by the {@code value} attribute
 * of an {@code <input>} element with {@code name="lang_id"} and CSS class
 * {@code "b0"}, which is nested inside a specific structure on the page:
 * a {@code <div>} with {@code id="ej-submit-tabs"}, containing a {@code <form>},
 * which contains a {@code <table>}. The extracted value is guaranteed to be
 * non-empty via {@link NonEmpty}, then converted to a number using
 * {@link NumOfText} and labelled as "problem's preset language" through
 * {@link NumAbout}.
 * <p>
 * A {@code PresetLang} can also be created by wrapping an existing {@link Num},
 * in which case it simply delegates to that number.
 */
public final class PresetLang implements Num {

    /**
     * The underlying numeric value.
     */
    private final Num origin;

    /**
     * Creates a preset language identifier by extracting it from the given
     * problem page using the provided XML engine.
     * <p>
     * The extraction follows a specific XPath selection:
     * <ol>
     *   <li>Locate a {@code <div>} with {@code id="ej-submit-tabs"}.</li>
     *   <li>Within it, find a {@code <form>}.</li>
     *   <li>Within that form, find a {@code <table>}.</li>
     *   <li>Within that table, find an {@code <input>} with class {@code "b0"}
     *       and {@code name="lang_id"}.</li>
     *   <li>Extract the {@code value} attribute of that input.</li>
     * </ol>
     * The resulting string is required to be non-empty, is converted to an
     * integer, and is labelled as "problem's preset language".
     *
     * @param e the XML engine used to evaluate the XPath selection
     * @param p the problem page from which the preset language id is extracted
     */
    public PresetLang(final XmlEngine e, final ProblemPage p) {
        this(
            new NumAbout(
                "problem's preset language",
                new NumOfText(
                    new NonEmpty(
                        new XmlSelection(
                            e, p,
                            new ValuesOnly(
                                new WithName(
                                    "lang_id",
                                    new NestedTag(
                                        "input",
                                        new WithClass(
                                            "b0",
                                            new NestedTag(
                                                "table",
                                                new NestedTag(
                                                    "form",
                                                    new WithId(
                                                        "ej-submit-tabs",
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
        );
    }

    /**
     * Creates a preset language identifier by wrapping an existing numeric
     * value.
     *
     * @param n the numeric value to wrap
     */
    public PresetLang(final Num n) {
        this.origin = n;
    }

    /**
     * Returns the numeric value of this preset language identifier.
     *
     * @return the preset language id as an integer
     * @throws InvariantViolation if an invariant is violated while retrieving
     *         the value
     */
    @Override
    public int value() throws InvariantViolation {
        return this.origin.value();
    }
}
