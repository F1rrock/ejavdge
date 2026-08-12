package org.ejavdge.web.context;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.web.media.Lift;
import org.ejavdge.web.media.Media;

public final class Union implements Context {
    private final Context left;
    private final Context right;

    public Union(final Context l, final Context r) {
        this.left = l;
        this.right = r;
    }

    @Override
    public <T> T imprint(final Media<T> m) throws InvariantViolation {
        return this.right.imprint(
            this.left.imprint(new Lift<>(m))
        );
    }
}
