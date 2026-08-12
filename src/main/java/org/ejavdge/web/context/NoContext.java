package org.ejavdge.web.context;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.web.media.Media;

public final class NoContext implements Context {
    @Override
    public <T> T imprint(final Media<T> m) throws InvariantViolation {
        return m.content();
    }
}
