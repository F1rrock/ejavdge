package org.ejavdge.web.context;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.scalar.num.Positive;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.scalar.text.TextAbout;
import org.ejavdge.scalar.text.TextOfNum;
import org.ejavdge.web.media.Media;

/**
 * A web context that represents the language identifier used when submitting a
 * solution to the ejudge contest system.
 * <p>
 * This class implements {@link Context} and supplies a single named entry,
 * {@code "lang_id"}, whose value is the numeric identifier of the programming
 * language or compiler to be used for the submission. When imprinted onto a
 * {@link Media}, this entry is attached and is typically used as a form field
 * in a solution submission request.
 * <p>
 * The numeric value is validated to be strictly positive via {@link Positive}
 * and is labelled with the subject {@code "language id"} via {@link TextAbout},
 * so that any failure while materializing it is reported with a meaningful
 * message. The value can be supplied either as a plain {@code int} or as a
 * {@link Num} instance, allowing it to be computed lazily if desired.
 * <p>
 * An instance can also be created by wrapping an already existing
 * {@link Context}, in which case the wrapping is transparent and all imprint
 * operations are delegated to that context.
 */
public final class LangId implements Context {

    /**
     * The underlying context that performs the actual imprint operation.
     */
    private final Context origin;

    /**
     * Creates a language identifier context from the given integer.
     *
     * @param n the numeric language identifier, must be strictly positive
     */
    public LangId(final int n) {
        this(new Num.Of(n));
    }

    /**
     * Creates a language identifier context from the given numeric value.
     * <p>
     * The value is validated to be strictly positive via {@link Positive},
     * converted to its decimal string representation via {@link TextOfNum},
     * labelled with the subject {@code "language id"} via {@link TextAbout},
     * and finally paired with the parameter name {@code "lang_id"} via
     * {@link WithEntry}.
     *
     * @param n the numeric language identifier, must be strictly positive
     */
    public LangId(final Num n) {
        this(
            new WithEntry(
                new Text.Of("lang_id"),
                new TextAbout(
                    "language id",
                    new TextOfNum(
                        new Positive(n)
                    )
                )
            )
        );
    }

    /**
     * Creates a language identifier context by wrapping an existing context.
     *
     * @param c the underlying context
     */
    public LangId(final Context c) {
        this.origin = c;
    }

    /**
     * Imprints this language identifier context onto the given media.
     * <p>
     * This method delegates to the underlying context's
     * {@link Context#imprint(Media)} method, which attaches the
     * {@code "lang_id"} entry to the media in a manner appropriate for the
     * media type.
     *
     * @param <T> the type of the result produced by the imprint operation
     * @param m   the media to imprint this context onto
     * @return the result of imprinting the underlying context onto the media
     * @throws InvariantViolation if an invariant is violated during the imprint
     *         operation, for example if the language identifier is not positive
     */
    @Override
    public <T> T imprint(final Media<T> m) throws InvariantViolation {
        return this.origin.imprint(m);
    }
}
