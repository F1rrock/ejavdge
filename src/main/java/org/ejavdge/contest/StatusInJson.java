package org.ejavdge.contest;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.scalar.text.TextAbout;
import org.ejavdge.scalar.text.TextOfNum;
import org.ejavdge.scalar.text.Utf8Text;
import org.ejavdge.web.context.WithEntry;
import org.ejavdge.web.resource.PayloadOf;

/**
 * The status of a run in the ejudge contest system, represented in JSON.
 * <p>
 * This class implements {@link Text} and provides access to the JSON-formatted
 * status of a run. The status is fetched from a {@link ContestResource} using
 * the query parameters {@code action=175} and {@code x=1}. The response payload
 * is then decoded as UTF-8 text and labelled as "run status".
 * <p>
 * A {@code StatusInJson} can be created either by fetching the status through a
 * contest resource or by wrapping an existing text.
 */
public final class StatusInJson implements Text {

    /**
     * The underlying textual content of the run status.
     */
    private final Text origin;

    /**
     * Creates a run status by fetching it from the given contest resource.
     * <p>
     * The resource is extended with a context that adds the query parameters
     * {@code action=175} and {@code x=1}. The response payload is then decoded
     * as UTF-8 text and wrapped with a descriptive label "run status".
     *
     * @param r the contest resource used to fetch the run status
     */
    public StatusInJson(final ContestResource r) {
        this(
            new TextAbout(
                "run status",
                new Utf8Text(
                    new PayloadOf(
                        new ContestResource(
                            r,
                            new WithEntry(
                                new Text.Of("action"),
                                new TextOfNum(175),
                                new WithEntry(
                                    new Text.Of("x"),
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
     * Creates a run status from existing text.
     *
     * @param t the text that will become the run status content
     */
    public StatusInJson(final Text t) {
        this.origin = t;
    }

    /**
     * Returns the textual content of the run status.
     *
     * @return the run status as a JSON string
     * @throws InvariantViolation if an invariant is violated while retrieving
     *         the content
     */
    @Override
    public String content() throws InvariantViolation {
        return this.origin.content();
    }
}
