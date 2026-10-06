package org.ejavdge.web.resource;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.web.context.Location;
import org.ejavdge.web.driver.WebDriver;
import org.ejavdge.web.spec.Request;

/**
 * A {@link Bytes} adapter that fetches a web resource by delegating to a
 * {@link WebDriver}.
 * <p>
 * This class binds together the three ingredients needed to perform an HTTP
 * request: a {@link WebDriver} that knows how to send it, a {@link Location}
 * that identifies the target resource, and a {@link Request} that describes the
 * method, headers, and body. When {@link #content()} is called, it simply
 * invokes {@link WebDriver#resourceOf(Location, Request)} and returns the raw
 * response bytes.
 * <p>
 * This is the primary bridge between the transport layer (the web driver) and
 * the rest of the application, which works with resources as {@link Bytes}
 * values. It is used, for example, by
 * {@link org.ejavdge.contest.ContestResource} and
 * {@link org.ejavdge.auth.LoginReply} to model specific kinds of requests while
 * reusing the same driver abstraction.
 */
public final class WebResource implements Bytes {

    /**
     * The web driver used to send the request.
     */
    private final WebDriver driver;

    /**
     * The location of the target resource.
     */
    private final Location loc;

    /**
     * The request to send, including method, headers, and body.
     */
    private final Request req;

    /**
     * Creates a new web resource from the given driver, location, and request.
     *
     * @param d the web driver used to send the request
     * @param l the location of the target resource
     * @param r the request to send
     */
    public WebResource(final WebDriver d, final Location l, final Request r) {
        this.driver = d;
        this.loc = l;
        this.req = r;
    }

    /**
     * Returns the raw response bytes obtained by sending the request through the
     * driver.
     * <p>
     * This method delegates directly to
     * {@link WebDriver#resourceOf(Location, Request)} and returns its result
     * unchanged.
     *
     * @return the raw response bytes as returned by the driver
     * @throws InvariantViolation if an invariant is violated during the request
     */
    @Override
    public byte[] content() throws InvariantViolation {
        return this.driver.resourceOf(this.loc, this.req);
    }
}
