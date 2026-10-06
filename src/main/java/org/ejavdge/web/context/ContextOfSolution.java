package org.ejavdge.web.context;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.web.media.Media;

/**
 * A web context that combines the problem identifier and the language
 * identifier required for submitting a solution.
 * <p>
 * When submitting a solution to the ejudge contest system, the request must
 * include both the problem identifier (to specify which problem the solution is
 * for) and the language identifier (to specify which compiler or language
 * should be used). This class represents that combination as a single
 * {@link Context}, so it can be imprinted onto a {@link Media} — for example,
 * as form fields in a multipart POST request.
 * <p>
 * The combination is built using a {@link Union} of the two underlying
 * contexts: the problem identifier ({@link ProbId}) and the language identifier
 * ({@link LangId}). This means the resulting context behaves as if both were
 * applied in sequence.
 * <p>
 * An instance can also be created by wrapping an already existing
 * {@link Context}, in which case the wrapping is transparent and all imprint
 * operations are delegated to that context.
 */
public final class ContextOfSolution implements Context {

    /**
     * The underlying context that performs the actual imprint operation.
     */
    private final Context origin;

    /**
     * Creates a solution context from the given problem and language
     * identifiers.
     * <p>
     * The two identifiers are combined into a single {@link Union} context,
     * which is then used as the underlying context. This constructor is a
     * convenience for the common case of submitting a solution where both the
     * problem and the language are known.
     *
     * @param p the problem identifier
     * @param l the language identifier
     */
    public ContextOfSolution(final ProbId p, final LangId l) {
        this(new Union(p, l));
    }

    /**
     * Creates a solution context by wrapping an existing context.
     *
     * @param c the underlying context
     */
    public ContextOfSolution(final Context c) {
        this.origin = c;
    }

    /**
     * Imprints this solution context onto the given media.
     * <p>
     * This method delegates to the underlying context's
     * {@link Context#imprint(Media)} method, which applies both the problem
     * identifier and the language identifier to the media in a manner
     * appropriate for the media type.
     *
     * @param <T> the type of the result produced by the imprint operation
     * @param m   the media to imprint this context onto
     * @return the result of imprinting the underlying context onto the media
     * @throws InvariantViolation if an invariant is violated during the imprint
     *         operation
     */
    @Override
    public <T> T imprint(Media<T> m) throws InvariantViolation {
        return this.origin.imprint(m);
    }
}
