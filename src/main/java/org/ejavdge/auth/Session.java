package org.ejavdge.auth;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.*;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.web.context.Credentials;
import org.ejavdge.web.context.Location;
import org.ejavdge.web.driver.WebDriver;
import org.ejavdge.web.resource.HasStatus;

import java.time.Duration;

/**
 * An authenticated session with the contest system.
 * <p>
 * A session is represented as raw bytes (typically cookies or a session
 * identifier) that are required for subsequent authenticated requests.
 * It can be created by performing a login request through a {@link WebDriver}
 * or by wrapping existing session bytes.
 */
public final class Session implements Bytes {

    /**
     * The underlying byte content of the session.
     */
    private final Bytes src;

    /**
     * Creates a session by logging in to the contest system.
     * <p>
     * This constructor performs a login request using the given driver,
     * location, and credentials. The login reply is expected to be an HTTP 302
     * redirect (which indicates successful authentication). The request is
     * wrapped with several decorators:
     * <ul>
     *   <li>{@link HasStatus} – verifies the expected status code and provides
     *       an error message for invalid credentials;</li>
     *   <li>{@link WithTimeout} – sets a 5-second timeout for the request;</li>
     *   <li>{@link Verbose} – logs a message while fetching the session;</li>
     *   <li>{@link WithRetries} – retries the request up to 5 times;</li>
     *   <li>{@link Memo} – memoizes the result to avoid repeated logins;</li>
     *   <li>{@link BytesAbout} – attaches a descriptive label to the bytes.</li>
     * </ul>
     *
     * @param d the web driver used to execute the login request
     * @param l the location of the contest system
     * @param c the credentials to authenticate with
     */
    public Session(final WebDriver d, final Location l, final Credentials c) {
        this(
            new BytesAbout(
                "ejudge session",
                new Memo(
                    new WithRetries(
                        new Verbose(
                            new WithTimeout(
                                new HasStatus(
                                    new Num.Of(302),
                                    new Text.Of("There is invalid credentials."),
                                    new LoginReply(d, l, c)
                                ),
                                Duration.ofSeconds(5)
                            ),
                            new Text.Of("Fetching session...")
                        ),
                        new Num.Of(5)
                    )
                )
            )
        );
    }

    /**
     * Creates a session from existing byte content.
     *
     * @param bs the byte content representing the session
     */
    public Session(final Bytes bs) {
        this.src = bs;
    }

    /**
     * Returns the raw byte content of this session.
     *
     * @return the session bytes
     * @throws InvariantViolation if an invariant is violated
     */
    @Override
    public byte[] content() throws InvariantViolation {
        return this.src.content();
    }
}
