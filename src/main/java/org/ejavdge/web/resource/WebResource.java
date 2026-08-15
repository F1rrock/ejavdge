package org.ejavdge.web.resource;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.web.context.Location;
import org.ejavdge.web.driver.WebDriver;
import org.ejavdge.web.spec.Request;

public final class WebResource implements Bytes {
    private final WebDriver driver;
    private final Location loc;
    private final Request req;

    public WebResource(final WebDriver d, final Location l, final Request r) {
        this.driver = d;
        this.loc = l;
        this.req = r;
    }

    @Override
    public byte[] content() throws InvariantViolation {
        return this.driver.resourceOf(this.loc, this.req);
    }
}
