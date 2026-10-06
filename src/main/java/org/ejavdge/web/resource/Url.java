package org.ejavdge.web.resource;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.*;
import org.ejavdge.web.context.Context;
import org.ejavdge.web.context.NoContext;
import org.ejavdge.web.context.Union;
import org.ejavdge.web.media.Form;

/**
 * A {@link Text} that represents a URL with an optional query string built from
 * a {@link Context}.
 * <p>
 * This class models a URL as a base text (the part before any query parameters)
 * together with a query context that supplies additional name-value pairs. When
 * {@link #content()} is called, the base URL is materialized, the query context
 * is imprinted onto a {@link Form} and encoded as
 * {@code application/x-www-form-urlencoded}, and the resulting query string is
 * appended to the base after a {@code "?"} separator. If the query context
 * produces no parameters, the URL is returned as just the base text, without
 * the trailing question mark.
 * <p>
 * A URL can be created directly from a string or a text, in which case it has
 * no query parameters. It can also be created by extending an existing URL with
 * a new query context: the new context is combined with the existing one via
 * {@link Union}, so both sets of parameters are included in the final URL.
 * <p>
 * The URL content is wrapped in a {@link TextAbout} with the subject
 * {@code "url"}, and the base is additionally wrapped with the subject
 * {@code "base"} and required to be non-empty via {@link NonEmpty}. If the base
 * is empty, an {@link InvariantViolation} with the message
 * {@code "There is empty URL."} is thrown.
 */
public final class Url implements Text {

    /**
     * The base part of the URL, before any query parameters.
     */
    private final Text base;

    /**
     * The context supplying the query parameters of the URL.
     */
    private final Context query;

    /**
     * Creates a new URL by extending an existing URL with additional query
     * parameters.
     * <p>
     * The base of the new URL is copied from the given URL, and the query
     * parameters are combined by unioning the existing query with the new one.
     * This allows a URL to be progressively refined with additional parameters
     * without losing the ones already present.
     *
     * @param u     the existing URL to extend
     * @param query the additional query context to append
     */
    public Url(final Url u, final Context query) {
        this.base = u.base;
        this.query = new Union(u.query, query);
    }

    /**
     * Creates a URL from the given string, with no query parameters.
     *
     * @param s the string representation of the base URL
     */
    public Url(final String s) {
        this(new Text.Of(s));
    }

    /**
     * Creates a URL from the given text, with no query parameters.
     *
     * @param t the base URL as text
     */
    public Url(final Text t) {
        this.base = t;
        this.query = new NoContext();
    }

    /**
     * Returns the full URL as a string, including the query string if any
     * parameters are present.
     * <p>
     * The base URL is materialized and required to be non-empty. The query
     * context is imprinted onto a {@link Form} and encoded as
     * {@code application/x-www-form-urlencoded}; the resulting string is then
     * appended to the base after a {@code "?"} separator. If the query
     * produces no parameters, an empty text is used instead, so no trailing
     * {@code "?"} appears. The result is wrapped with the descriptive subject
     * {@code "url"} for diagnostics.
     *
     * @return the full URL as a string
     * @throws InvariantViolation if the base URL is empty or if an invariant
     *         is violated while materializing the base or the query parameters
     */
    @Override
    public String content() throws InvariantViolation {
        return new TextAbout(
            "url",
            new Concat(
                new TextAbout(
                    "base",
                    new NonEmpty(
                        this.base,
                        new Text.Of("There is empty URL.")
                    )
                ),
                new BindOfText(
                    new Utf8Text(
                        new Form.ImprintOf(this.query)
                    ),
                    s -> new Fallback(
                        new BindOfText(
                            new NonEmpty(new Text.Of(s)),
                            p -> new Concat(
                                new Text.Of("?"),
                                new Text.Of(p)
                            )
                        ),
                        new Empty()
                    )
                )
            )
        ).content();
    }
}
