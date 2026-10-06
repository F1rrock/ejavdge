package org.ejavdge.web.context;

import org.ejavdge.auth.Session;
import org.ejavdge.domain.tokens.Sid;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.web.media.Media;

/**
 * A web context that represents the {@code SID} session identifier used by the
 * ejudge contest system.
 * <p>
 * In the ejudge system, the {@code SID} value is typically passed as a query
 * parameter in URLs to identify the current authenticated session. This class
 * adapts that value into a {@link Context} so that it can be imprinted onto a
 * {@link Media}, for example as a query parameter when constructing a request.
 * <p>
 * The class provides a chain of convenience constructors that accept the
 * session identifier in progressively more abstract forms: raw {@code byte[]}
 * data, a {@link Bytes} sequence, a {@link Session}, an already extracted
 * {@link Sid} token, or a fully formed {@link Context}. Each constructor
 * delegates to the next, ultimately building a {@link WithEntry} that pairs
 * the parameter name {@code "SID"} with the identifier value.
 */
public final class ContextOfSid implements Context {

    /**
     * The underlying context that performs the actual imprint operation.
     */
    private final Context origin;

    /**
     * Creates a context for the {@code SID} parameter from the given raw bytes.
     *
     * @param bs the raw bytes containing the session, from which the
     *           {@code SID} value is extracted
     */
    public ContextOfSid(final byte[] bs) {
        this(new Bytes.Of(bs));
    }

    /**
     * Creates a context for the {@code SID} parameter from the given byte
     * sequence.
     *
     * @param bs the byte sequence containing the session, from which the
     *           {@code SID} value is extracted
     */
    public ContextOfSid(final Bytes bs) {
        this(new Session(bs));
    }

    /**
     * Creates a context for the {@code SID} parameter from the given session.
     *
     * @param s the authenticated session from which the {@code SID} value is
     *          extracted
     */
    public ContextOfSid(final Session s) {
        this(new Sid(s));
    }

    /**
     * Creates a context for the {@code SID} parameter from the given token.
     * <p>
     * The token is paired with the parameter name {@code "SID"} via
     * {@link WithEntry}, producing a context that can be imprinted onto any
     * media that supports named entries, such as HTTP query parameters.
     *
     * @param s the {@code SID} token to use as the session identifier value
     */
    public ContextOfSid(final Sid s) {
        this(
            new WithEntry(
                new Text.Of("SID"),
                s
            )
        );
    }

    /**
     * Creates a context for the {@code SID} parameter by wrapping an existing
     * context.
     *
     * @param c the underlying context
     */
    public ContextOfSid(final Context c) {
        this.origin = c;
    }

    /**
     * Imprints this {@code SID} context onto the given media.
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
