package org.ejavdge.web.spec.body.multipart;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.items.Items;
import org.ejavdge.items.Joint;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.web.context.Context;
import org.ejavdge.web.media.Media;

import java.util.List;

/**
 * A {@link Media} that accumulates plain text fields as multipart parts.
 * <p>
 * This media is the multipart counterpart of {@link org.ejavdge.web.media.Form}:
 * each call to {@link #with(Text, Text)} appends a new {@link TextPart}
 * representing a single name-value entry, and the accumulated parts are
 * exposed as a {@link List} via {@link #content()}. Because the class is
 * immutable, {@code with} returns a new {@code TextParts} instance with the
 * additional part appended, leaving the original untouched.
 * <p>
 * The resulting list is typically consumed by
 * {@link org.ejavdge.web.spec.body.multipart.Multipart} when assembling a
 * {@code multipart/form-data} request body, or combined with file parts
 * via a {@link org.ejavdge.items.Joint} collection.
 * <p>
 * The nested {@link ImprintOf} class provides a convenient way to convert a
 * {@link Context} into a list of {@code Part}s: it creates an empty
 * {@code TextParts} instance, imprints the context onto it, and returns the
 * resulting list.
 */
public final class TextParts implements Media<List<Part>> {

    /**
     * The collection of parts accumulated so far.
     */
    private final Items<Part> src;

    /**
     * Creates an empty collection of text parts.
     */
    public TextParts() {
        this(new Items.Of<>());
    }

    /**
     * Creates a collection of text parts from the given parts.
     * <p>
     * This constructor is typically used internally when extending an
     * existing collection with additional parts.
     *
     * @param ps the parts that make up this collection
     */
    public TextParts(final Items<Part> ps) {
        this.src = ps;
    }

    /**
     * Returns a new collection of text parts that includes all parts from
     * this collection plus a new text part with the given name and value.
     * <p>
     * The new part is created as a {@link TextPart} and appended to the
     * existing collection. A new {@code TextParts} instance is returned,
     * leaving the original unmodified.
     *
     * @param n the name of the form field for the new part
     * @param v the value of the form field for the new part
     * @return a new {@code TextParts} instance containing the additional
     *         part
     * @throws InvariantViolation if an invariant is violated while
     *         constructing the new part
     */
    @Override
    public TextParts with(final Text n, final Text v) throws InvariantViolation {
        return new TextParts(
            new Joint<>(
                this.src,
                new Items.Of<>(
                    new TextPart(n, v)
                )
            )
        );
    }

    /**
     * Returns the list of parts accumulated in this collection.
     *
     * @return the list of multipart parts
     * @throws InvariantViolation if an invariant is violated while
     *         materializing the parts
     */
    @Override
    public List<Part> content() throws InvariantViolation {
        return this.src.contents();
    }

    /**
     * An {@link Items} that imprints a {@link Context} onto a new
     * {@code TextParts} instance and returns the resulting list of parts.
     * <p>
     * This class is useful for converting a context that carries form-like
     * entries (such as credentials or solution metadata) into a list of
     * multipart parts suitable for inclusion in a
     * {@code multipart/form-data} request body. It creates an empty
     * {@code TextParts} instance, applies the context via
     * {@link Context#imprint(Media)}, and then returns the resulting list
     * of parts.
     */
    public static final class ImprintOf implements Items<Part> {

        /**
         * The context whose entries are imprinted as text parts.
         */
        private final Context ctx;

        /**
         * The target collection that receives the context's entries.
         */
        private final TextParts ps;

        /**
         * Creates a text-parts imprint from the given context.
         * <p>
         * An empty {@code TextParts} instance is created and will be
         * populated when the context is imprinted.
         *
         * @param c the context to imprint as text parts
         */
        public ImprintOf(final Context c) {
            this.ctx = c;
            this.ps = new TextParts();
        }

        /**
         * Imprints the context onto the text parts and returns the
         * resulting list of parts.
         * <p>
         * The context is applied via {@link Context#imprint(Media)},
         * which adds its entries as text parts to the internal
         * collection. The list of collected parts is then returned.
         *
         * @return the list of parts collected from the context
         * @throws InvariantViolation if an invariant is violated during
         *         the imprint operation or while materializing the parts
         */
        @Override
        public List<Part> contents() throws InvariantViolation {
            return this.ctx.imprint(this.ps);
        }
    }
}
