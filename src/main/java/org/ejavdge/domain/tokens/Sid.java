package org.ejavdge.domain.tokens;

import org.ejavdge.auth.Session;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Match;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.scalar.text.TextAbout;
import org.ejavdge.scalar.text.Utf8Text;

/**
 * A textual representation of the {@code SID} session identifier used by the
 * ejudge contest system.
 * <p>
 * After authentication, the ejudge server issues a session identifier that is
 * typically passed as the {@code SID} query parameter in URLs. This class
 * extracts that value from the raw session bytes and exposes it as a
 * {@link Text}. The extraction is performed using the regular expression
 * {@code (?<=[?&]SID=)[^&]+}, which captures everything after either
 * {@code ?SID=} or {@code &SID=} up to the next ampersand.
 * <p>
 * The resulting value is labelled as {@code "sid"} via {@link TextAbout}.
 * <p>
 * A {@code Sid} instance can also be created by wrapping an existing
 * {@link Text} that already contains the session identifier value.
 */
public final class Sid implements Text {

    /**
     * The underlying textual content representing the {@code SID} value.
     */
    private final Text origin;

    /**
     * Creates a {@code SID} value by extracting it from the given session.
     * <p>
     * The session bytes are decoded as UTF-8 text, and the value of the
     * {@code SID} query parameter is extracted using the regular expression
     * {@code (?<=[?&]SID=)[^&]+}. The extracted value is labelled as
     * {@code "sid"}.
     *
     * @param s the authenticated session from which the {@code SID} value is
     *          extracted
     */
    public Sid(final Session s) {
        this(
            new TextAbout(
                "sid",
                new Match(
                    new Utf8Text(s),
                    new Text.Of("(?<=[?&]SID=)[^&]+")
                )
            )
        );
    }

    /**
     * Creates a {@code SID} value by wrapping an existing text.
     *
     * @param t the text that will become the {@code SID} value content
     */
    public Sid(final Text t) {
        this.origin = t;
    }

    /**
     * Returns the textual content of the {@code SID} value.
     *
     * @return the {@code SID} value as a string
     * @throws InvariantViolation if an invariant is violated while retrieving
     *         the content, or if the value cannot be extracted
     */
    @Override
    public String content() throws InvariantViolation {
        return this.origin.content();
    }
}
