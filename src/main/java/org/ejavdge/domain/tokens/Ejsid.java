package org.ejavdge.domain.tokens;

import org.ejavdge.auth.Session;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Match;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.scalar.text.TextAbout;
import org.ejavdge.scalar.text.Utf8Text;

/**
 * A textual representation of the {@code EJSID} session token used by the
 * ejudge contest system.
 * <p>
 * When an authenticated {@link Session} is established, the server sets an
 * {@code EJSID} cookie that identifies the session. This class extracts the
 * value of that cookie from the raw session bytes and exposes it as a
 * {@link Text}. The extraction is performed using the regular expression
 * {@code (?<=EJSID=)[^;]+}, which captures everything after {@code EJSID=} up
 * to the next semicolon.
 * <p>
 * The resulting value is labelled as {@code "ejsid"} via {@link TextAbout}.
 * <p>
 * An {@code Ejsid} instance can also be created by wrapping an existing
 * {@link Text} that already contains the token value.
 */
public final class Ejsid implements Text {

    /**
     * The underlying textual content representing the {@code EJSID} token.
     */
    private final Text origin;

    /**
     * Creates an {@code EJSID} token by extracting it from the given session.
     * <p>
     * The session bytes are decoded as UTF-8 text, and the value of the
     * {@code EJSID} cookie is extracted using the regular expression
     * {@code (?<=EJSID=)[^;]+}. The extracted value is labelled as
     * {@code "ejsid"}.
     *
     * @param s the authenticated session from which the {@code EJSID} token is
     *          extracted
     */
    public Ejsid(final Session s) {
        this(
            new TextAbout(
                "ejsid",
                new Match(
                    new Utf8Text(s),
                    new Text.Of("(?<=EJSID=)[^;]+")
                )
            )
        );
    }

    /**
     * Creates an {@code EJSID} token by wrapping an existing text.
     *
     * @param t the text that will become the {@code EJSID} token content
     */
    public Ejsid(final Text t) {
        this.origin = t;
    }

    /**
     * Returns the textual content of the {@code EJSID} token.
     *
     * @return the {@code EJSID} token value as a string
     * @throws InvariantViolation if an invariant is violated while retrieving
     *         the content, or if the token cannot be extracted
     */
    @Override
    public String content() throws InvariantViolation {
        return this.origin.content();
    }
}
