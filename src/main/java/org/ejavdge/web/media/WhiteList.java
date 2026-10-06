package org.ejavdge.web.media;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.items.Items;
import org.ejavdge.items.Map;
import org.ejavdge.scalar.text.ContentBased;
import org.ejavdge.scalar.text.Text;

/**
 * A {@link Media} decorator that filters named entries, allowing only those
 * whose names are present in a configured whitelist to pass through to the
 * underlying media.
 * <p>
 * This class is useful when only a subset of the entries supplied by a
 * {@link org.ejavdge.web.context.Context} should be applied to a media. For
 * example, when constructing a request that requires only the {@code "host"}
 * entry from a {@link org.ejavdge.web.context.Location} context, a
 * {@code WhiteList} containing just that name can be used to discard all other
 * entries.
 * <p>
 * The whitelist is stored as a collection of {@link ContentBased} text values,
 * so that membership is determined by string content rather than object
 * identity. When {@link #with(Text, Text)} is called with a name-value pair,
 * the name is compared against the whitelist: if it matches, the pair is
 * forwarded to the underlying media and a new {@code WhiteList} is returned
 * wrapping the result; if it does not match, the current instance is returned
 * unchanged, effectively dropping the entry.
 * <p>
 * The {@link #content()} method simply delegates to the underlying media, so
 * the whitelist is transparent with respect to the final materialized value.
 *
 * @param <T> the type of value produced by the underlying media
 */
public final class WhiteList<T> implements Media<T> {

    /**
     * The collection of allowed entry names, stored in a content-based form so
     * that membership is determined by string value.
     */
    private final Items<ContentBased> members;

    /**
     * The underlying media to which allowed entries are forwarded.
     */
    private final Media<T> origin;

    /**
     * Creates a new whitelist decorator by copying the allowed names from an
     * existing whitelist and wrapping the given media.
     * <p>
     * This constructor is used internally when forwarding an allowed entry to
     * the underlying media, so that the resulting decorator retains the same
     * whitelist and wraps the newly extended media.
     *
     * @param l the existing whitelist from which the allowed names are copied
     * @param m the underlying media to wrap
     */
    public WhiteList(final WhiteList<T> l, final Media<T> m) {
        this.members = l.members;
        this.origin = m;
    }

    /**
     * Creates a new whitelist decorator with a single allowed name.
     *
     * @param x the single name to allow through
     * @param m the underlying media to wrap
     */
    public WhiteList(final Text x, final Media<T> m) {
        this(new Items.Of<>(x), m);
    }

    /**
     * Creates a new whitelist decorator with the given collection of allowed
     * names.
     * <p>
     * The names are wrapped in {@link ContentBased} so that membership testing
     * is based on string content rather than object identity.
     *
     * @param xs the names to allow through
     * @param m  the underlying media to wrap
     */
    public WhiteList(final Items<Text> xs, final Media<T> m) {
        this.members = new Map<>(ContentBased::new, xs);
        this.origin = m;
    }

    /**
     * Returns a new whitelist that includes the given named entry if its name
     * is present in the whitelist, or the current instance otherwise.
     * <p>
     * The name is compared against the allowed names using content-based
     * equality. If the name matches, the entry is forwarded to the underlying
     * media via {@link Media#with(Text, Text)}, and a new {@code WhiteList} is
     * returned wrapping the resulting media. If the name does not match, the
     * entry is silently dropped and the current instance is returned unchanged.
     *
     * @param n the name of the entry to consider
     * @param v the value of the entry to consider
     * @return a new {@code WhiteList} containing the entry if it is allowed,
     *         or the current instance if it is not
     * @throws InvariantViolation if an invariant is violated while checking
     *         membership or while adding the entry to the underlying media
     */
    @Override
    public WhiteList<T> with(final Text n, final Text v) throws InvariantViolation {
        if (this.members.contents().contains(new ContentBased(n))) {
            return new WhiteList<>(
                this,
                this.origin.with(n, v)
            );
        }
        return this;
    }

    /**
     * Returns the final value produced by the underlying media.
     * <p>
     * The whitelist is transparent with respect to the materialized content:
     * this method simply delegates to the underlying media's
     * {@link Media#content()} method.
     *
     * @return the materialized content of the underlying media
     * @throws InvariantViolation if an invariant is violated while materializing
     *         the content
     */
    @Override
    public T content() throws InvariantViolation {
        return this.origin.content();
    }
}
