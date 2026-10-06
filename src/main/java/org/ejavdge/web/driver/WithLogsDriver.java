package org.ejavdge.web.driver;

import org.ejavdge.scalar.bytes.Memo;
import org.ejavdge.web.spec.ByteView;
import org.slf4j.Logger;

import org.ejavdge.scalar.text.Utf8Text;
import org.ejavdge.web.context.Location;
import org.ejavdge.web.spec.Request;

/**
 * A {@link WebDriver} decorator that logs the outgoing request bytes before
 * delegating to another driver.
 * <p>
 * This class wraps an existing {@link WebDriver} and a {@link Logger}. When a
 * request is performed via {@link #resourceOf(Location, Request)}, the raw
 * bytes of the request are materialized and, if trace logging is enabled, are
 * logged as UTF-8 text. The request is then passed to the underlying driver for
 * actual execution.
 * <p>
 * To avoid evaluating the request bytes more than once (for example, once for
 * logging and once for sending), the bytes are wrapped in a {@link Memo} backed
 * by a {@link ByteView}. This ensures that the request is rendered to bytes at
 * most once, and the same byte array is used for both logging and delegation.
 * <p>
 * This driver is useful for debugging and auditing the exact requests sent to
 * the contest system, without modifying the behavior of the underlying driver.
 */
public final class WithLogsDriver implements WebDriver {

    /**
     * The underlying web driver that performs the actual request.
     */
    private final WebDriver origin;

    /**
     * The logger used to emit the request bytes at trace level.
     */
    private final Logger log;

    /**
     * Creates a logging driver that decorates the given driver with the
     * specified logger.
     *
     * @param d the underlying web driver to delegate requests to
     * @param l the logger used to log request bytes at trace level
     */
    public WithLogsDriver(final WebDriver d, final Logger l) {
        this.origin = d;
        this.log = l;
    }

    /**
     * Performs the given request, logging its bytes if trace logging is enabled,
     * and delegates to the underlying driver.
     * <p>
     * The request is first wrapped in a {@link ByteView} and memoized via
     * {@link Memo}, so its byte representation is computed at most once. If the
     * logger has trace level enabled, the request bytes are decoded as UTF-8
     * and logged. Finally, a new {@link Request} is constructed from the
     * memoized bytes and passed to the underlying driver's
     * {@link WebDriver#resourceOf(Location, Request)} method.
     *
     * @param loc the location of the target resource
     * @param req the request to send
     * @return the raw response bytes as returned by the underlying driver
     */
    @Override
    public byte[] resourceOf(final Location loc, final Request req) {
        final var bs = new Memo(new ByteView(req));
        if (log.isTraceEnabled()) {
            this.log.trace(
                new Utf8Text(bs).content()
            );
        }
        return this.origin.resourceOf(loc, new Request(bs));
    }
}
