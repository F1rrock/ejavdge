package org.ejavdge.contest;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.scalar.text.TextAbout;
import org.ejavdge.scalar.text.TextOfNum;
import org.ejavdge.scalar.text.Utf8Text;
import org.ejavdge.web.context.ProbId;
import org.ejavdge.web.context.WithEntry;
import org.ejavdge.web.resource.PayloadOf;

/**
 * A page displaying a specific problem in the ejudge contest system.
 * <p>
 * This class implements {@link Text} and represents the textual content of a
 * problem page. The page is fetched from a {@link ContestResource} using the
 * given {@link ProbId} and the query parameter {@code action=139}. The response
 * payload is then decoded as UTF-8 text and labelled as "problem page".
 * <p>
 * A {@code ProblemPage} can be created either by fetching the page through a
 * contest resource and a problem identifier, or by wrapping an existing text.
 */
public final class ProblemPage implements Text {

    /**
     * The underlying textual content of the problem page.
     */
    private final Text origin;

    /**
     * Creates a problem page by fetching it from the given contest resource
     * using the specified problem identifier.
     * <p>
     * The resource is extended with a context that adds the query parameter
     * {@code action=139} and the problem ID. The response payload is then
     * decoded as UTF-8 text and wrapped with a descriptive label
     * "problem page".
     *
     * @param r the contest resource used to fetch the problem page
     * @param p the identifier of the problem whose page is requested
     */
    public ProblemPage(final ContestResource r, final ProbId p) {
        this(
            new TextAbout(
                "problem page",
                new Utf8Text(
                    new PayloadOf(
                        new ContestResource(
                            r,
                            new WithEntry(
                                new Text.Of("action"),
                                new TextOfNum(139),
                                p
                            )
                        )
                    )
                )
            )
        );
    }

    /**
     * Creates a problem page from existing text.
     *
     * @param t the text that will become the problem page content
     */
    public ProblemPage(final Text t) {
        this.origin = t;
    }

    /**
     * Returns the textual content of the problem page.
     *
     * @return the problem page content as a string
     * @throws InvariantViolation if an invariant is violated while retrieving
     *         the content
     */
    @Override
    public String content() throws InvariantViolation {
        return this.origin.content();
    }
}
