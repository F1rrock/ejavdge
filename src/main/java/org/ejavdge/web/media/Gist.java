package org.ejavdge.web.media;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.items.Items;
import org.ejavdge.items.Joint;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.web.context.Context;

import java.util.List;

/**
 * A {@link Media} that collects only the values from a {@link Context},
 * discarding the associated names.
 * <p>
 * Unlike {@link Form} or {@link Cookies}, which preserve both names and values
 * in their encoded output, a {@code Gist} retains only the values. When a
 * name-value pair is added via {@link #with(Text, Text)}, the name is ignored
 * and only the value is appended to the underlying collection. The final
 * content is a list of {@link Text} values in the order they were added.
 * <p>
 * This is useful when only the values carried by a context are needed, without
 * regard to the keys under which they are stored. For example, extracting the
 * host and port from a {@link org.ejavdge.web.context.Location} context as a
 * list of values, or collecting all values from a context for further
 * processing.
 * <p>
 * The nested {@link ImprintOf} class provides a convenient way to obtain the
 * list of values from a context. It creates an empty {@code Gist} instance,
 * imprints the context onto it (which adds the context's values), and returns
 * the resulting list.
 */
public final class Gist implements Media<List<Text>> {

    /**
     * The collection of values collected from the context.
     */
    private final Items<Text> src;

    /**
     * Creates an empty gist.
     */
    public Gist() {
        this(new Items.Of<>());
    }

    /**
     * Creates a gist from the given collection of text values.
     * <p>
     * This constructor is typically used internally when extending an existing
     * gist with additional values.
     *
     * @param src the collection of text values
     */
    public Gist(final Items<Text> src) {
        this.src = src;
    }

    /**
     * Returns a new gist that includes all values from this gist plus the
     * specified value.
     * <p>
     * The name is ignored; only the value is appended to the collection. A new
     * {@code Gist} instance is returned, leaving the original unmodified.
     *
     * @param n the name associated with the value (ignored)
     * @param v the value to append
     * @return a new {@code Gist} instance containing the additional value
     */
    @Override
    public Gist with(Text n, Text v) {
        return new Gist(
            new Joint<>(
                this.src,
                new Items.Of<>(v)
            )
        );
    }

    /**
     * Returns the list of values collected in this gist.
     *
     * @return the list of text values
     * @throws InvariantViolation if an invariant is violated while materializing
     *         the values
     */
    @Override
    public List<Text> content() throws InvariantViolation {
        return this.src.contents();
    }

    /**
     * An {@link Items} that imprints a {@link Context} onto a new {@code Gist}
     * instance and returns the resulting list of values.
     * <p>
     * This class is useful for converting a context into a list of its values,
     * discarding the associated names. It creates an empty {@code Gist}
     * instance, applies the context via {@link Context#imprint(Media)}, and
     * then returns the list of collected values.
     */
    public static final class ImprintOf implements Items<Text> {

        /**
         * The context whose values are collected.
         */
        private final Context ctx;

        /**
         * The target gist that receives the context's values.
         */
        private final Gist gist;

        /**
         * Creates a gist imprint from the given context.
         * <p>
         * An empty {@code Gist} instance is created and will be populated when
         * the context is imprinted.
         *
         * @param c the context to imprint
         */
        public ImprintOf(final Context c) {
            this.ctx = c;
            this.gist = new Gist();
        }

        /**
         * Imprints the context onto the gist and returns the resulting list of
         * values.
         * <p>
         * The context is applied via {@link Context#imprint(Media)}, which adds
         * its values to the internal gist. The list of collected values is then
         * returned.
         *
         * @return the list of text values collected from the context
         * @throws InvariantViolation if an invariant is violated during the
         *         imprint operation or while materializing the values
         */
        @Override
        public List<Text> contents() throws InvariantViolation {
            return this.ctx.imprint(this.gist);
        }
    }
}
