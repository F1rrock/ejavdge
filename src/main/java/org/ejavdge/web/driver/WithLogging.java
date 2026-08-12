package org.ejavdge.web.driver;

import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.text.Utf8Text;
import org.ejavdge.web.context.Location;
import org.ejavdge.web.spec.Request;

public final class WithLogging implements WebDriver {
    private final WebDriver origin;

    public WithLogging(final WebDriver origin) {
        this.origin = origin;
    }

    @Override
    public byte[] resourceOf(final Location loc, final Request req) {
        System.out.println(
            new Utf8Text(
                new Bytes.Of(
                    req.bytes()
                )
            ).content()
        );
        return this.origin.resourceOf(loc, req);
    }
}
