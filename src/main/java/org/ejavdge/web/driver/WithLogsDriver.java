package org.ejavdge.web.driver;

import org.ejavdge.scalar.bytes.Memo;
import org.ejavdge.web.spec.ByteView;
import org.slf4j.Logger;

import org.ejavdge.scalar.text.Utf8Text;
import org.ejavdge.web.context.Location;
import org.ejavdge.web.spec.Request;

public final class WithLogsDriver implements WebDriver {
    private final WebDriver origin;
    private final Logger log;

    public WithLogsDriver(final WebDriver d, final Logger l) {
        this.origin = d;
        this.log = l;
    }

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
