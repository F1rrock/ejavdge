package org.ejavdge.app.setup;

import org.ejavdge.web.context.Location;
import org.ejavdge.web.driver.WebDriver;
import org.ejavdge.web.driver.WithLogsDriver;
import org.ejavdge.web.driver.jdk.socket.JdkSocket;
import org.ejavdge.web.spec.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The default {@link WebDriver} used by apps that do not need a
 * custom transport.
 *
 * <p>Wraps {@link JdkSocket} — a plain socket-based HTTP transport
 * — with {@link WithLogsDriver}, so every request and response is
 * logged through SLF4J. This is the driver wired into apps when the
 * caller does not pass one explicitly.
 *
 * <p>Tests that need to intercept requests, or apps that need a
 * different transport, bypass this class and pass their own
 * {@code WebDriver} to the scenario constructors.
 */
public final class PresetDriver implements WebDriver {
    private final WebDriver origin;

    /**
     * Uses the logger named after this class.
     */
    public PresetDriver() {
        this(LoggerFactory.getLogger(PresetDriver.class));
    }

    /**
     * @param l the logger to send request and response entries to
     */
    public PresetDriver(final Logger l) {
        this.origin = new WithLogsDriver(
            new JdkSocket(),
            l
        );
    }

    @Override
    public byte[] resourceOf(Location loc, Request req) {
        return this.origin.resourceOf(loc, req);
    }
}
