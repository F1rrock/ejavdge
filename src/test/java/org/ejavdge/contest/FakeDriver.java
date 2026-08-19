package org.ejavdge.contest;

import org.ejavdge.web.context.Location;
import org.ejavdge.web.driver.WebDriver;
import org.ejavdge.web.spec.Request;

import java.util.Arrays;

public final class FakeDriver implements WebDriver {
    private final byte[] expected;
    private final byte[] left;
    private final byte[] right;

    public FakeDriver(final byte[] ex, final byte[] l, final byte[] r) {
        this.expected = ex.clone();
        this.left = l.clone();
        this.right = r.clone();
    }

    @Override
    public byte[] resourceOf(final Location loc, final Request req) {
        if (Arrays.equals(req.bytes(), this.expected)) {
            return this.right.clone();
        } else {
            return this.left.clone();
        }
    }
}
