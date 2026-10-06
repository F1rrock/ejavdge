package org.ejavdge.domain.problem;

import org.ejavdge.contest.ProblemPage;
import org.ejavdge.dom.XmlSelection;
import org.ejavdge.dom.engine.XmlEngine;
import org.ejavdge.dom.path.*;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.num.*;

/**
 * A numeric value representing a problem's language identifier, selected by
 * index from a problem page.
 * <p>
 * This class implements {@link Num} and extracts the {@code value} attribute of
 * the {@code <option>} element at a given position inside the
 * {@code <select name="lang_id">} element on a problem page. The index is
 * zero-based: index {@code 0} selects the first option, {@code 1} the second,
 * and so on.
 * <p>
 * The extraction is performed using an XPath expression built from the provided
 * {@link ProblemPage} and evaluated by an {@link XmlEngine}. The resulting value
 * is wrapped with a descriptive label "problem's language id" via
 * {@link NumAbout} and converted from text using {@link NumOfText}.
 * <p>
 * A {@code LangByIndex} can also be created by wrapping an existing {@link Num},
 * in which case it simply delegates to that number.
 */
public final class LangByIndex implements Num {

    /**
     * The underlying numeric value.
     */
    private final Num origin;

    /**
     * Creates a language identifier by selecting the option at the given index
     * from the problem page.
     * <p>
     * The provided index {@code n} is zero-based. Internally, it is incremented
     * by one to match XPath's 1-based indexing. The selection locates the
     * {@code <select>} element whose {@code name} attribute is {@code lang_id},
     * then selects its {@code n}-th child element (zero-based), and finally
     * extracts the {@code value} attribute of that element.
     * <p>
     * The resulting text is interpreted as an integer and labelled as the
     * problem's language id.
     *
     * @param e the XML engine used to evaluate the XPath selection
     * @param p the problem page from which the language id is extracted
     * @param n the zero-based index of the option whose value is selected
     */
    public LangByIndex(final XmlEngine e, final ProblemPage p, final Num n) {
        this(
            new NumAbout(
                "problem's language id",
                new NumOfText(
                    new XmlSelection(
                        e, p,
                        new ValuesOnly(
                            new OnlyAt(
                                new SumOf(
                                    new Positive(n),
                                    new Num.Of(1)
                                ),
                                new ChildrenOf(
                                    new WithName(
                                        "lang_id",
                                        new OnlyTag("select")
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
     * Creates a language identifier by wrapping an existing numeric value.
     *
     * @param n the numeric value to wrap
     */
    public LangByIndex(final Num n) {
        this.origin = n;
    }

    /**
     * Returns the numeric value of this language identifier.
     *
     * @return the language id as an integer
     * @throws InvariantViolation if an invariant is violated while retrieving
     *         the value
     */
    @Override
    public int value() throws InvariantViolation {
        return this.origin.value();
    }
}
