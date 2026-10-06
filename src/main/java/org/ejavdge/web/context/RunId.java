package org.ejavdge.web.context;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.*;
import org.ejavdge.web.media.Media;

/**
 * A web context that represents the identifier of a run in the ejudge contest
 * system.
 * <p>
 * This class implements {@link Context} and supplies a single named entry,
 * {@code "run_id"}, whose value is the identifier of a run (submission). When
 * imprinted onto a {@link Media}, this entry is attached and is typically used
 * as a query parameter or form field when requesting a run report.
 * <p>
 * The run identifier is extracted from the given text using a small parsing
 * rule: if the text contains a {@code #$} suffix (for example, a fragment
 * identifier appended by the browser), everything before the first occurrence
 * of {@code #$} is taken as the run id. If no such suffix is present, the
 * entire text is used. In both cases, the resulting value is required to be
 * non-empty via {@link NonEmpty}, and is labelled with the subject
 * {@code "run id"} via {@link TextAbout} for diagnostic purposes.
 * <p>
 * The value can be supplied either as a plain {@link String} or as a
 * {@link Text} instance, allowing it to be computed lazily if desired.
 */
public final class RunId implements Context {

    /**
     * The run identifier, wrapped and validated as non-empty.
     */
    private final Text src;

    /**
     * Creates a run identifier context from the given string.
     *
     * @param s the string representation of the run identifier
     */
    public RunId(final String s) {
        this(new Text.Of(s));
    }

    /**
     * Creates a run identifier context from the given text.
     * <p>
     * The text is parsed to extract the actual run identifier: if it contains a
     * {@code #$} suffix, only the part before that suffix is retained;
     * otherwise, the whole text is used. The extracted value is required to be
     * non-empty, and is labelled with the subject {@code "run id"}.
     *
     * @param t the text representing the run identifier
     */
    public RunId(final Text t) {
        this.src = new TextAbout(
            "run id",
            new NonEmpty(
                new BindOfText(
                    t,
                    id -> new Fallback(
                        new Match(
                            new Text.Of(id),
                            new Text.Of("^.*?(?=#$)")
                        ),
                        new Text.Of(id)
                    )
                ),
                new Text.Of("there is no run id")
            )
        );
    }

    /**
     * Imprints this run identifier onto the given media.
     * <p>
     * The run identifier is attached to the media as an entry named
     * {@code "run_id"}.
     *
     * @param <T> the type of the result produced by the imprint operation
     * @param m   the media to imprint this run identifier onto
     * @return the result of imprinting the run identifier onto the media
     * @throws InvariantViolation if an invariant is violated during the imprint
     *         operation, for example if the run identifier is empty
     */
    @Override
    public <T> T imprint(final Media<T> m) throws InvariantViolation {
        return m
            .with(new Text.Of("run_id"), this.src)
            .content();
    }
}
