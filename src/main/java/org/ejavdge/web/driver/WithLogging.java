package org.ejavdge.web.driver;

import org.slf4j.Logger;

import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.text.Utf8Text;
import org.ejavdge.web.context.Location;
import org.ejavdge.web.spec.Request;

public final class WithLogging implements WebDriver {
    private final WebDriver origin;
    private final Logger log;

    public WithLogging(final WebDriver d, final Logger l) {
        this.origin = d;
        this.log = l;
    }

    @Override
    public byte[] resourceOf(final Location loc, final Request req) {
        if (log.isTraceEnabled()) {
            this.log.trace(
                new Utf8Text(
                    new Bytes.Of(
                        req.bytes()
                    )
                ).content()
            );
        }
        return this.origin.resourceOf(loc, req);
    }
}
