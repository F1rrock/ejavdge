package org.ejavdge.web.context;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.web.media.Lift;
import org.ejavdge.web.media.Media;

/**
 * A {@link Context} that combines two other contexts, applying them in
 * sequence when imprinted onto a {@link Media}.
 * <p>
 * This class represents the logical union (or composition) of two contexts:
 * a left context and a right context. When {@link #imprint(Media)} is called,
 * the left context is imprinted first, and the result is then passed to the
 * right context for a second imprint operation. This allows multiple pieces of
 * context information — such as a problem identifier and a language identifier,
 * or credentials and a location — to be attached to the same media in a single
 * operation.
 * <p>
 * The composition is achieved with the help of a {@link Lift}, which adapts
 * the media produced by the first imprint so that it can be consumed by the
 * second. The order of application is significant: the left context is
 * evaluated before the right context, and the right context can rely on the
 * entries already attached by the left context if needed.
 * <p>
 * This class is typically used internally by higher-level context
 * implementations (such as {@link ContextOfSolution}) to combine several
 * simpler contexts into one, but it can also be used directly whenever two
 * contexts need to be applied together.
 */
public final class Union implements Context {

    /**
     * The context applied first during imprinting.
     */
    private final Context left;

    /**
     * The context applied second during imprinting.
     */
    private final Context right;

    /**
     * Creates a union of the given left and right contexts.
     * <p>
     * When imprinted, the left context is applied first, followed by the right
     * context.
     *
     * @param l the context to apply first
     * @param r the context to apply second
     */
    public Union(final Context l, final Context r) {
        this.left = l;
        this.right = r;
    }

    /**
     * Imprints this union onto the given media by applying the left context
     * first, then the right context.
     * <p>
     * The media is first wrapped in a {@link Lift}, then passed to the left
     * context's {@link Context#imprint(Media)} method. The result of that
     * operation is then passed to the right context's
     * {@link Context#imprint(Media)} method, and the final result is returned.
     * This allows both contexts to contribute their data to the same logical
     * imprint operation.
     *
     * @param <T> the type of the result produced by the imprint operation
     * @param m   the media to imprint this union onto
     * @return the result of imprinting both contexts onto the media
     * @throws InvariantViolation if an invariant is violated during either
     *         imprint operation
     */
    @Override
    public <T> T imprint(final Media<T> m) throws InvariantViolation {
        return this.right.imprint(
            this.left.imprint(new Lift<>(m))
        );
    }
}
