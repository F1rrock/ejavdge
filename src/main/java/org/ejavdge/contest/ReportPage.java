package org.ejavdge.contest;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.scalar.text.TextAbout;
import org.ejavdge.scalar.text.TextOfNum;
import org.ejavdge.scalar.text.Utf8Text;
import org.ejavdge.web.context.RunId;
import org.ejavdge.web.context.WithEntry;
import org.ejavdge.web.resource.PayloadOf;

/**
 * A page displaying the report of a specific run in the ejudge contest system.
 * <p>
 * This class implements {@link Text} and represents the textual content of a
 * run report page. The page is fetched from a {@link ContestResource} using the
 * given {@link RunId} and the query parameter {@code action=37}. The response
 * payload is then decoded as UTF-8 text and labelled as "report page".
 * <p>
 * A {@code ReportPage} can be created either by fetching the page through a
 * contest resource and a run identifier, or by wrapping an existing text.
 */
public final class ReportPage implements Text {

    /**
     * The underlying textual content of the report page.
     */
    private final Text origin;

    /**
     * Creates a report page by fetching it from the given contest resource
     * using the specified run identifier.
     * <p>
     * The resource is extended with a context that adds the query parameter
     * {@code action=37} and the run ID. The response payload is then decoded
     * as UTF-8 text and wrapped with a descriptive label "report page".
     *
     * @param cr the contest resource used to fetch the report page
     * @param id the identifier of the run whose report is requested
     */
    public ReportPage(final ContestResource cr, final RunId id) {
        this(
            new TextAbout(
                "report page",
                new Utf8Text(
                    new PayloadOf(
                        new ContestResource(
                            cr,
                            new WithEntry(
                                new Text.Of("action"),
                                new TextOfNum(37),
                                id
                            )
                        )
                    )
                )
            )
        );
    }

    /**
     * Creates a report page from existing text.
     *
     * @param t the text that will become the report page content
     */
    public ReportPage(final Text t) {
        this.origin = t;
    }

    /**
     * Returns the textual content of the report page.
     *
     * @return the report page content as a string
     * @throws InvariantViolation if an invariant is violated while retrieving
     *         the content
     */
    @Override
    public String content() throws InvariantViolation {
        return this.origin.content();
    }
}
