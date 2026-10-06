package org.ejavdge.web.media;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.items.Items;
import org.ejavdge.items.Joint;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.bytes.Utf8;
import org.ejavdge.scalar.text.Concat;
import org.ejavdge.scalar.text.Stencil;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.scalar.text.PartOfUrl;
import org.ejavdge.web.context.Context;

/**
 * A {@link Media} that represents an HTTP form body encoded as
 * {@code application/x-www-form-urlencoded}.
 * <p>
 * Form data consists of a sequence of name-value pairs, where both the name and
 * the value are URL-encoded and joined by {@code =}. Multiple pairs are
 * separated by {@code &}. This class models such a form as an immutable
 * collection of pre-formatted fragments, each of the form {@code name=value}.
 * <p>
 * When imprinted with a name-value pair via {@link #with(Text, Text)}, a new
 * {@code Form} instance is returned that contains all previous pairs plus the
 * new one. The final byte content is produced by joining all pairs with the
 * separator {@code "&"} and encoding the result as UTF-8 bytes, as required by
 * the {@code application/x-www-form-urlencoded} media type.
 * <p>
 * The nested {@link ImprintOf} class provides a convenient way to build a form
 * body from a {@link Context}. It creates an empty {@code Form} instance,
 * imprints the context onto it (which adds the context's entries as form
 * fields), and returns the resulting byte array. This is used when a context
 * needs to be rendered as a form-encoded request body, for example when
 * submitting a login form or a solution.
 */
public final class Form implements Media<byte[]> {

    /**
     * The collection of form field fragments, each formatted as
     * {@code name=value}.
     */
    private final Items<Text> src;

    /**
     * Creates an empty form.
     */
    public Form() {
        this(new Items.Of<>());
    }

    /**
     * Creates a form from the given collection of text fragments.
     * <p>
     * Each fragment is expected to be a pre-formatted form field of the form
     * {@code name=value}. This constructor is typically used internally when
     * extending an existing form with additional fields.
     *
     * @param src the collection of pre-formatted form field fragments
     */
    public Form(final Items<Text> src) {
        this.src = src;
    }

    /**
     * Returns a new form that includes all fields from this form plus the
     * specified name-value pair.
     * <p>
     * Both the name and the value are URL-encoded via {@link PartOfUrl} and
     * formatted as {@code name=value} using a {@link Stencil}. The resulting
     * fragment is appended to the existing collection, and a new {@code Form}
     * instance is returned. The original instance is not modified.
     *
     * @param n the field name
     * @param v the field value
     * @return a new {@code Form} instance containing the additional field
     */
    @Override
    public Form with(final Text n, final Text v) {
        return new Form(
            new Joint<>(
                this.src,
                new Items.Of<>(
                    new Stencil(
                        new Text.Of("%s=%s"),
                        new PartOfUrl(n),
                        new PartOfUrl(v)
                    )
                )
            )
        );
    }

    /**
     * Returns the form content as a UTF-8 encoded byte array.
     * <p>
     * All form field fragments are materialized and joined together using the
     * separator {@code "&"}, producing a string suitable for use as the body of
     * an HTTP request with content type
     * {@code application/x-www-form-urlencoded}. The resulting string is then
     * encoded as UTF-8 bytes.
     *
     * @return the form-encoded body as a byte array
     * @throws InvariantViolation if an invariant is violated while materializing
     *         the form field fragments
     */
    @Override
    public byte[] content() throws InvariantViolation {
        return new Utf8(
            new Concat(
                new Text.Of("&"),
                this.src
            )
        ).content();
    }

    /**
     * A {@link Bytes} that imprints a {@link Context} onto a new {@code Form}
     * instance and returns the resulting form-encoded byte array.
     * <p>
     * This class is useful for converting a context that carries form-like
     * entries (such as credentials or solution metadata) into a properly
     * formatted form body. It creates an empty {@code Form} instance, applies
     * the context via {@link Context#imprint(Media)}, and then materializes the
     * resulting form as bytes.
     */
    public static final class ImprintOf implements Bytes {

        /**
         * The context whose entries are imprinted as form fields.
         */
        private final Context ctx;

        /**
         * The target form that receives the context's entries.
         */
        private final Form form;

        /**
         * Creates a form imprint from the given context.
         * <p>
         * An empty {@code Form} instance is created and will be populated when
         * the context is imprinted.
         *
         * @param c the context to imprint as form fields
         */
        public ImprintOf(final Context c) {
            this.ctx = c;
            this.form = new Form();
        }

        /**
         * Imprints the context onto the form and returns the resulting
         * form-encoded byte array.
         * <p>
         * The context is applied via {@link Context#imprint(Media)}, which adds
         * its entries as form fields to the internal form. The content of the
         * populated form is then returned as UTF-8 encoded bytes.
         *
         * @return the form-encoded body as a byte array
         * @throws InvariantViolation if an invariant is violated during the
         *         imprint operation or while materializing the form
         */
        @Override
        public byte[] content() throws InvariantViolation {
            return this.ctx.imprint(this.form);
        }
    }
}
