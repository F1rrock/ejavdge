package org.ejavdge.web.context;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.web.media.Media;

/**
 * A {@link Context} that attaches a single named entry to a {@link Media} and
 * optionally delegates to an underlying context.
 * <p>
 * This class represents the basic building block for constructing web contexts:
 * a name-value pair that is added to a media when imprinted. The name and value
 * are both {@link Text} instances, allowing them to be computed lazily. An
 * optional underlying context can be supplied; when present, it is imprinted
 * after the entry has been attached, enabling the composition of multiple
 * entries in a chain.
 * <p>
 * When {@link #imprint(Media)} is called, the entry is attached to the media
 * via {@link Media#with(Text, Text)}, and the resulting media is then passed to
 * the underlying context's {@link Context#imprint(Media)} method. If no
 * underlying context is provided, a {@link NoContext} is used by default, which
 * simply returns the media's content unchanged.
 * <p>
 * This class is used throughout the application to build contexts such as
 * credentials ({@code login}, {@code password}, {@code contest_id}), tokens
 * ({@code EJSID}, {@code SID}), problem and language identifiers, and other
 * named values required for interacting with the contest system.
 */
public final class WithEntry implements Context {

    /**
     * The name of the entry to attach to the media.
     */
    private final Text name;

    /**
     * The value of the entry to attach to the media.
     */
    private final Text value;

    /**
     * The underlying context to imprint after attaching the entry.
     */
    private final Context origin;

    /**
     * Creates a context that attaches the given named entry to a media, with no
     * underlying context.
     * <p>
     * This constructor is a convenience for the common case of a single
     * name-value pair with no further context to apply. A {@link NoContext} is
     * used as the underlying context.
     *
     * @param n the name of the entry
     * @param v the value of the entry
     */
    public WithEntry(final Text n, final Text v) {
        this(n, v, new NoContext());
    }

    /**
     * Creates a context that attaches the given named entry to a media and then
     * delegates to the given underlying context.
     *
     * @param n the name of the entry
     * @param v the value of the entry
     * @param c the underlying context to imprint after attaching the entry
     */
    public WithEntry(final Text n, final Text v, final Context c) {
        this.name = n;
        this.value = v;
        this.origin = c;
    }

    /**
     * Imprints this entry onto the given media and then applies the underlying
     * context.
     * <p>
     * The entry is first attached to the media via
     * {@link Media#with(Text, Text)}, producing a new media that includes the
     * name-value pair. That media is then passed to the underlying context's
     * {@link Context#imprint(Media)} method, and the final result is returned.
     * This allows multiple entries to be composed in a chain by nesting
     * {@code WithEntry} instances.
     *
     * @param <T> the type of the result produced by the imprint operation
     * @param m   the media to imprint this entry onto
     * @return the result of imprinting the entry and the underlying context
     *         onto the media
     * @throws InvariantViolation if an invariant is violated during the imprint
     *         operation, for example if the name or value cannot be materialized
     */
    @Override
    public <T> T imprint(final Media<T> m) throws InvariantViolation {
        return this.origin.imprint(m.with(this.name, this.value));
    }
}
