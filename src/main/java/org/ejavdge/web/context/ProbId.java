package org.ejavdge.web.context;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.scalar.num.NumAbout;
import org.ejavdge.scalar.num.Positive;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.scalar.text.TextOfNum;
import org.ejavdge.web.media.Media;

/**
 * A web context that represents the identifier of a problem in the ejudge
 * contest system.
 * <p>
 * This class implements {@link Context} and supplies a single named entry,
 * {@code "prob_id"}, whose value is the numeric identifier of a problem. When
 * imprinted onto a {@link Media}, this entry is attached and is typically used
 * as a query parameter or form field when requesting a problem page or
 * submitting a solution.
 * <p>
 * The numeric value is validated to be strictly positive via {@link Positive}
 * and is labelled with the subject {@code "problem id"} via {@link NumAbout},
 * so that any failure while materializing it is reported with a meaningful
 * message. The value can be supplied either as a plain {@code int} or as a
 * {@link Num} instance, allowing it to be computed lazily if desired — for
 * example, by resolving a problem name to its identifier via
 * {@link org.ejavdge.domain.problem.ProbByName}.
 */
public final class ProbId implements Context {

    /**
     * The numeric problem identifier, wrapped and validated as positive.
     */
    private final Num src;

    /**
     * Creates a problem identifier context from the given integer.
     *
     * @param n the numeric problem identifier, must be strictly positive
     */
    public ProbId(final int n) {
        this(new Num.Of(n));
    }

    /**
     * Creates a problem identifier context from the given numeric value.
     * <p>
     * The value is validated to be strictly positive via {@link Positive} and
     * labelled with the subject {@code "problem id"} via {@link NumAbout}.
     *
     * @param n the numeric problem identifier, must be strictly positive
     */
    public ProbId(final Num n) {
        this.src = new NumAbout(
            "problem id",
            new Positive(n)
        );
    }

    /**
     * Imprints this problem identifier onto the given media.
     * <p>
     * The problem identifier is attached to the media as an entry named
     * {@code "prob_id"}, converted from its numeric form to text.
     *
     * @param <T> the type of the result produced by the imprint operation
     * @param m   the media to imprint this problem identifier onto
     * @return the result of imprinting the problem identifier onto the media
     * @throws InvariantViolation if an invariant is violated during the imprint
     *         operation, for example if the problem identifier is not positive
     */
    @Override
    public <T> T imprint(final Media<T> m) throws InvariantViolation {
        return m
            .with(new Text.Of("prob_id"), new TextOfNum(this.src))
            .content();
    }
}
