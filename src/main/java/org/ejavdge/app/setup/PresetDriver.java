package org.ejavdge.app.setup;

import org.ejavdge.web.context.Location;
import org.ejavdge.web.driver.WebDriver;
import org.ejavdge.web.driver.WithLogging;
import org.ejavdge.web.driver.jdk.socket.JdkSocket;
import org.ejavdge.web.spec.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class PresetDriver implements WebDriver {
    private final WebDriver origin;

    public PresetDriver() {
        this(LoggerFactory.getLogger(PresetDriver.class));
    }

    public PresetDriver(final Logger l) {
        this.origin = new WithLogging(
            new JdkSocket(),
            l
        );
    }

    @Override
    public byte[] resourceOf(Location loc, Request req) {
        return this.origin.resourceOf(loc, req);
    }
}
