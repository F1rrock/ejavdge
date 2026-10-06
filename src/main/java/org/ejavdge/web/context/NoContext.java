package org.ejavdge.web.context;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.web.media.Media;

/**
 * A {@link Context} that adds no data when imprinted onto a {@link Media}.
 * <p>
 * This class represents the neutral or identity context. When its
 * {@link #imprint(Media)} method is called, it simply returns the media's
 * content unchanged, without attaching any entries. It is useful as a default
 * context, as a base case in context compositions, or whenever a context is
 * required but no additional data should be added.
 */
public final class NoContext implements Context {

    /**
     * The inner context that performs the actual imprint operation.
     * <p>
     * For an empty context, this is a no-op implementation that simply
     * returns the media's content unchanged.
     */
    private final Context origin;

    /**
     * Creates a new empty context.
     * <p>
     * The context is stateless: it carries no entries of its own and
     * leaves the media it is imprinted onto unchanged.
     */
    public NoContext() {
        this(new Empty());
    }

    /**
     * Creates a context that delegates to the given inner context.
     * <p>
     * This constructor is intended for internal composition only; the
     * resulting instance is not an empty context and will forward its
     * imprint operation to the supplied context.
     *
     * @param c the context to delegate to
     */
    private NoContext(final Context c) {
        this.origin = c;
    }

    /**
     * Imprints this empty context onto the given media.
     * <p>
     * Since this context carries no data, the media is returned unchanged
     * via {@link Media#content()}.
     *
     * @param <T> the type of the result produced by the imprint operation
     * @param m   the media to imprint this context onto
     * @return the media's content, unchanged
     * @throws InvariantViolation if an invariant is violated while
     *         retrieving the media's content
     */
    @Override
    public <T> T imprint(final Media<T> m) throws InvariantViolation {
        return this.origin.imprint(m);
    }

    /**
     * A no-op context that returns the media's content unchanged.
     */
    private static final class Empty implements Context {

        /**
         * Creates a new no-op context.
         */
        private Empty() {
        }

        @Override
        public <T> T imprint(final Media<T> m) throws InvariantViolation {
            return m.content();
        }
    }
}
