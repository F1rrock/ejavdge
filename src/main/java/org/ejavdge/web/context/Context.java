package org.ejavdge.web.context;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.web.media.Media;

@FunctionalInterface
public interface Context {
    <T> T imprint(final Media<T> m) throws InvariantViolation;
}
