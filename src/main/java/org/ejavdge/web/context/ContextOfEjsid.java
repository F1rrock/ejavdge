package org.ejavdge.web.context;

import org.ejavdge.auth.Session;
import org.ejavdge.domain.tokens.Ejsid;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.web.media.Media;

/**
 * A web context that represents the {@code EJSID} cookie used by the ejudge
 * contest system.
 * <p>
 * In the ejudge system, the {@code EJSID} cookie identifies the current
 * authenticated session. This class adapts that cookie into a {@link Context}
 * so that it can be imprinted onto a {@link Media}, for example as an HTTP
 * header when constructing a request.
 * <p>
 * The class provides a chain of convenience constructors that accept the
 * cookie value in progressively more abstract forms: raw {@code byte[]} data,
 * a {@link Bytes} sequence, a {@link Session}, an already extracted
 * {@link Ejsid} token, or a fully formed {@link Context}. Each constructor
 * delegates to the next, ultimately building a {@link WithEntry} that pairs
 * the header name {@code "EJSID"} with the cookie value.
 */
public final class ContextOfEjsid implements Context {

    /**
     * The underlying context that performs the actual imprint operation.
     */
    private final Context origin;

    /**
     * Creates a context for the {@code EJSID} cookie from the given raw bytes.
     *
     * @param bs the raw bytes containing the session, from which the
     *           {@code EJSID} value is extracted
     */
    public ContextOfEjsid(final byte[] bs) {
        this(new Bytes.Of(bs));
    }

    /**
     * Creates a context for the {@code EJSID} cookie from the given byte
     * sequence.
     *
     * @param bs the byte sequence containing the session, from which the
     *           {@code EJSID} value is extracted
     */
    public ContextOfEjsid(final Bytes bs) {
        this(new Session(bs));
    }

    /**
     * Creates a context for the {@code EJSID} cookie from the given session.
     *
     * @param s the authenticated session from which the {@code EJSID} value is
     *          extracted
     */
    public ContextOfEjsid(final Session s) {
        this(new Ejsid(s));
    }

    /**
     * Creates a context for the {@code EJSID} cookie from the given token.
     * <p>
     * The token is paired with the header name {@code "EJSID"} via
     * {@link WithEntry}, producing a context that can be imprinted onto any
     * media that supports named entries, such as HTTP headers.
     *
     * @param s the {@code EJSID} token to use as the cookie value
     */
    public ContextOfEjsid(final Ejsid s) {
        this(
            new WithEntry(
                new Text.Of("EJSID"),
                s
            )
        );
    }

    /**
     * Creates a context for the {@code EJSID} cookie by wrapping an existing
     * context.
     *
     * @param c the underlying context
     */
    public ContextOfEjsid(final Context c) {
        this.origin = c;
    }

    /**
     * Imprints this {@code EJSID} context onto the given media.
     *
     * @param <T> the type of the result produced by the imprint operation
     * @param m   the media to imprint this context onto
     * @return the result of imprinting the underlying context onto the media
     * @throws InvariantViolation if an invariant is violated during the imprint
     *         operation
     */
    @Override
    public <T> T imprint(final Media<T> m) throws InvariantViolation {
        return this.origin.imprint(m);
    }
}
