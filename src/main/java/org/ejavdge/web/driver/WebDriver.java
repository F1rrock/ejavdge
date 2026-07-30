package org.ejavdge.web.driver;

import org.ejavdge.web.context.Location;
import org.ejavdge.web.spec.Request;

public interface WebDriver {
    byte[] resourceOf(final Location loc, final Request req);
}
