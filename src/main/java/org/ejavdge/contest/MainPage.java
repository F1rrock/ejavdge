package org.ejavdge.contest;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.scalar.text.TextAbout;
import org.ejavdge.scalar.text.TextOfNum;
import org.ejavdge.scalar.text.Utf8Text;
import org.ejavdge.web.context.WithEntry;
import org.ejavdge.web.resource.PayloadOf;

/**
 * The main page of the ejudge contest system.
 * <p>
 * This class implements {@link Text} and represents the textual content of the
 * contest's main page. The page is fetched from a {@link ContestResource} with
 * specific query parameters ({@code action=2} and {@code lt=1}), then decoded
 * as UTF-8 text and labelled as "main page".
 * <p>
 * A {@code MainPage} can be created either by fetching the page through a
 * contest resource or by wrapping an existing text.
 */
public final class MainPage implements Text {

    /**
     * The underlying textual content of the main page.
     */
    private final Text origin;

    /**
     * Creates a main page by fetching it from the given contest resource.
     * <p>
     * The resource is extended with a context that adds the query parameters
     * {@code action=2} and {@code lt=1}. The response payload is then decoded
     * as UTF-8 text and wrapped with a descriptive label "main page".
     *
     * @param r the contest resource used to fetch the main page
     */
    public MainPage(final ContestResource r) {
        this(
            new TextAbout(
                "main page",
                new Utf8Text(
                    new PayloadOf(
                        new ContestResource(
                            r,
                            new WithEntry(
                                new Text.Of("amp;action"),
                                new TextOfNum(2),
                                new WithEntry(
                                    new Text.Of("amp;lt"),
                                    new TextOfNum(1)
                                )
                            )
                        )
                    )
                )
            )
        );
    }

    /**
     * Creates a main page from existing text.
     *
     * @param t the text that will become the main page content
     */
    public MainPage(final Text t) {
        this.origin = t;
    }

    /**
     * Returns the textual content of the main page.
     *
     * @return the main page content as a string
     * @throws InvariantViolation if an invariant is violated while retrieving
     *         the content
     */
    @Override
    public String content() throws InvariantViolation {
        return this.origin.content();
    }
}
