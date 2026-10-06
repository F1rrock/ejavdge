package org.ejavdge.auth;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.web.context.Credentials;
import org.ejavdge.web.context.Location;
import org.ejavdge.web.context.WithEntry;
import org.ejavdge.web.driver.WebDriver;
import org.ejavdge.web.media.Form;
import org.ejavdge.web.resource.WebResource;
import org.ejavdge.web.spec.Request;
import org.ejavdge.web.spec.body.WithBody;
import org.ejavdge.web.spec.header.Header;
import org.ejavdge.web.spec.header.WithHeaders;
import org.ejavdge.web.spec.method.Post;

/**
 * The reply received from the contest system after attempting to log in.
 * <p>
 * This class implements {@link Bytes} and represents the raw response body
 * of a login request. It can be constructed either by performing an actual
 * login request through a {@link WebDriver} or by wrapping existing bytes.
 */
public final class LoginReply implements Bytes {

    /**
     * The underlying byte content of the reply.
     */
    private final Bytes origin;

    /**
     * Creates a login reply by sending a login request to the contest system.
     * <p>
     * A POST request is sent to the given location with a form body containing
     * the login action and the provided credentials. The reply is the raw
     * response body returned by the contest system.
     *
     * @param d the web driver used to execute the request
     * @param l the location of the contest system
     * @param c the credentials to authenticate with
     */
    public LoginReply(final WebDriver d, final Location l, final Credentials c) {
        this(
            new WebResource(
                d, l,
                new Request(
                    new WithBody(
                        new Form.ImprintOf(
                            new WithEntry(
                                new Text.Of("action_2"),
                                new Text.Of("Log in"),
                                c
                            )
                        ),
                        new WithHeaders(
                            new Header(
                                new Text.Of("Content-Type"),
                                new Text.Of("application/x-www-form-urlencoded")
                            ),
                            new Post(l)
                        )
                    )
                )
            )
        );
    }

    /**
     * Creates a login reply from existing byte content.
     *
     * @param origin the byte content of the reply
     */
    public LoginReply(final Bytes origin) {
        this.origin = origin;
    }

    /**
     * Returns the raw byte content of this reply.
     *
     * @return the reply body as a byte array
     * @throws InvariantViolation if an invariant is violated
     */
    @Override
    public byte[] content() throws InvariantViolation {
        return this.origin.content();
    }
}
