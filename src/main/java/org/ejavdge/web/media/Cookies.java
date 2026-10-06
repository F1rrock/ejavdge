package org.ejavdge.web.media;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.items.Items;
import org.ejavdge.items.Joint;
import org.ejavdge.scalar.text.Concat;
import org.ejavdge.scalar.text.Stencil;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.scalar.text.PartOfUrl;
import org.ejavdge.web.context.Context;

/**
 * A {@link Media} that represents a collection of HTTP cookies.
 * <p>
 * Cookies are name-value pairs that are typically sent in the {@code Cookie}
 * header of an HTTP request or set via the {@code Set-Cookie} header of an
 * HTTP response. This class models a sequence of such pairs, where each pair is
 * stored as a pre-formatted text fragment of the form {@code name=value}, with
 * both the name and the value URL-encoded via {@link PartOfUrl}.
 * <p>
 * When imprinted with a name-value pair via {@link #with(Text, Text)}, a new
 * {@code Cookies} instance is returned that contains all previous cookies plus
 * the new one. This makes the class immutable and suitable for functional-style
 * composition. The final textual content of the collection is produced by
 * joining all cookie fragments with the separator {@code "; "}, as required by
 * the HTTP specification for the {@code Cookie} header.
 * <p>
 * The nested {@link ImprintOf} class provides a convenient way to build a
 * cookie string from a {@link Context}. It creates an empty {@code Cookies}
 * instance, imprints the context onto it (which adds the context's entries as
 * cookies), and returns the resulting string. This is used when a context
 * (such as {@link org.ejavdge.web.context.ContextOfEjsid}) needs to be
 * rendered as a cookie header value.
 */
public final class Cookies implements Media<String> {

    /**
     * The collection of cookie fragments, each formatted as {@code name=value}.
     */
    private final Items<Text> src;

    /**
     * Creates an empty collection of cookies.
     */
    public Cookies() {
        this(new Items.Of<>());
    }

    /**
     * Creates a collection of cookies from the given collection of text
     * fragments.
     * <p>
     * Each fragment is expected to be a pre-formatted cookie string of the form
     * {@code name=value}. This constructor is typically used internally when
     * extending an existing collection with additional cookies.
     *
     * @param src the collection of pre-formatted cookie fragments
     */
    public Cookies(final Items<Text> src) {
        this.src = src;
    }

    /**
     * Returns a new collection of cookies that includes all cookies from this
     * collection plus the specified name-value pair.
     * <p>
     * Both the name and the value are URL-encoded via {@link PartOfUrl} and
     * formatted as {@code name=value} using a {@link Stencil}. The resulting
     * fragment is appended to the existing collection, and a new
     * {@code Cookies} instance is returned. The original instance is not
     * modified.
     *
     * @param n the cookie name
     * @param v the cookie value
     * @return a new {@code Cookies} instance containing the additional cookie
     */
    @Override
    public Cookies with(final Text n, final Text v) {
        return new Cookies(
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
     * Returns the textual content of this cookie collection.
     * <p>
     * All cookie fragments are materialized and joined together using the
     * separator {@code "; "}, producing a string suitable for use as the value
     * of an HTTP {@code Cookie} header.
     *
     * @return the cookies as a semicolon-separated string
     * @throws InvariantViolation if an invariant is violated while materializing
     *         the cookie fragments
     */
    @Override
    public String content() throws InvariantViolation {
        return new Concat(
            new Text.Of("; "),
            this.src
        ).content();
    }

    /**
     * A {@link Text} that imprints a {@link Context} onto a new
     * {@code Cookies} instance and returns the resulting cookie string.
     * <p>
     * This class is useful for converting a context that carries cookie-like
     * entries (such as a session token) into a properly formatted cookie header
     * value. It creates an empty {@code Cookies} instance, applies the context
     * via {@link Context#imprint(Media)}, and then materializes the resulting
     * cookies as text.
     */
    public static final class ImprintOf implements Text {

        /**
         * The context whose entries are imprinted as cookies.
         */
        private final Context ctx;

        /**
         * The target cookies collection that receives the context's entries.
         */
        private final Cookies cookies;

        /**
         * Creates a cookie imprint from the given context.
         * <p>
         * An empty {@code Cookies} instance is created and will be populated
         * when the context is imprinted.
         *
         * @param c the context to imprint as cookies
         */
        public ImprintOf(final Context c) {
            this.ctx = c;
            this.cookies = new Cookies();
        }

        /**
         * Imprints the context onto the cookie collection and returns the
         * resulting cookie string.
         * <p>
         * The context is applied via {@link Context#imprint(Media)}, which adds
         * its entries as cookies to the internal collection. The content of the
         * populated collection is then returned.
         *
         * @return the cookies as a semicolon-separated string
         * @throws InvariantViolation if an invariant is violated during the
         *         imprint operation or while materializing the cookies
         */
        @Override
        public String content() throws InvariantViolation {
            return this.ctx.imprint(this.cookies);
        }
    }
}
