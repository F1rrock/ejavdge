package org.ejavdge.contest;

import org.ejavdge.auth.Session;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.BindOfBytes;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.bytes.BytesAbout;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.web.context.*;
import org.ejavdge.web.driver.WebDriver;
import org.ejavdge.web.media.Cookies;
import org.ejavdge.web.resource.WebResource;
import org.ejavdge.web.spec.Request;
import org.ejavdge.web.spec.header.Header;
import org.ejavdge.web.spec.header.WithHeaders;
import org.ejavdge.web.spec.method.Get;

/**
 * A resource in the ejudge contest system that can be fetched with an
 * authenticated request.
 * <p>
 * This class encapsulates a web driver, a location, and an authenticated
 * session. It implements {@link Bytes} so that its raw content can be obtained
 * by performing a GET request to the contest system. The request includes the
 * session's {@code ejsid} cookie and the session ID ({@code sid}) in the URL
 * context.
 * <p>
 * A resource can be created from scratch or derived from an existing resource
 * by replacing its location with one that includes an additional context.
 */
public final class ContestResource implements Bytes {

    /**
     * The web driver used to execute requests.
     */
    private final WebDriver driver;

    /**
     * The location of the resource in the contest system.
     */
    private final Location location;

    /**
     * The authenticated session used for requests.
     */
    private final Session session;

    /**
     * Creates a new resource by copying the driver and session from an existing
     * resource and using a new location that extends the existing location
     * with the given context.
     *
     * @param r the existing resource to copy the driver and session from
     * @param q the additional context to append to the location
     */
    public ContestResource(final ContestResource r, final Context q) {
        this(
            r.driver,
            new Location(r.location, q),
            r.session
        );
    }

    /**
     * Creates a new resource with the given driver, location, and session.
     *
     * @param d the web driver used to execute requests
     * @param l the location of the resource in the contest system
     * @param s the authenticated session used for requests
     */
    public ContestResource(final WebDriver d, final Location l, final Session s) {
        this.driver = d;
        this.location = l;
        this.session = s;
    }

    /**
     * Returns the raw byte content of this resource.
     * <p>
     * The content is obtained by performing a GET request to the resource's
     * location, including the session ID ({@code sid}) in the URL context and
     * the session's {@code ejsid} cookie in the request headers.
     * <p>
     * The request is wrapped with several decorators:
     * <ul>
     *   <li>{@link BytesAbout} – attaches a descriptive label to the bytes;</li>
     *   <li>{@link BindOfBytes} – binds the session bytes to the request;</li>
     * </ul>
     *
     * @return the raw content of the resource as a byte array
     * @throws InvariantViolation if an invariant is violated during the request
     */
    @Override
    public byte[] content() throws InvariantViolation {
        return new BytesAbout(
            "contest resource",
            new BindOfBytes(
                this.session,
                s -> new WebResource(
                    this.driver,
                    this.location,
                    new Request(
                        new WithHeaders(
                            new Header(
                                new Text.Of("Cookie"),
                                new Cookies.ImprintOf(
                                    new ContextOfEjsid(s)
                                )
                            ),
                            new Get(
                                new Location(
                                    this.location,
                                    new ContextOfSid(s)
                                )
                            )
                        )
                    )
                )
            )
        ).content();
    }
}
