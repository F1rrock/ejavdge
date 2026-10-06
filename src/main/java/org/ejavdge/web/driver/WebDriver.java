package org.ejavdge.web.driver;

import org.ejavdge.web.context.Location;
import org.ejavdge.web.spec.Request;

/**
 * A contract for a web driver that performs HTTP requests and returns the raw
 * response bytes.
 * <p>
 * Implementations of this interface are responsible for executing a given
 * {@link Request} against a target {@link Location} and returning the full
 * response as a byte array. The driver abstracts away the details of how the
 * request is transmitted and how the response is read — for example, using
 * plain JDK sockets, a higher-level HTTP client, or a mock for testing.
 * <p>
 * This is a functional interface whose functional method is
 * {@link #resourceOf(Location, Request)}. It is used throughout the application
 * wherever a resource needs to be fetched from the contest system, such as by
 * {@link org.ejavdge.contest.ContestResource} when retrieving contest pages,
 * by {@link org.ejavdge.auth.LoginReply} when performing a login, and by
 * {@link org.ejavdge.web.resource.WebResource} when constructing higher-level
 * requests.
 */
@FunctionalInterface
public interface WebDriver {

    /**
     * Performs the given request against the specified location and returns the
     * raw response bytes.
     * <p>
     * The exact format of the returned byte array depends on the implementation.
     * Typically, it contains the full HTTP response, including the status line,
     * headers, and body, encoded in a way that downstream consumers can parse
     * — for example, using a terminator to separate the header block from the
     * body.
     *
     * @param loc the location of the target resource, including host, port, and
     *            URL
     * @param req the request to send, including method, headers, and body
     * @return the raw response bytes as returned by the server
     */
    byte[] resourceOf(final Location loc, final Request req);
}
