package org.ejavdge.web.media;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

public interface Media<T> {
    Media<T> with(final Text n, final Text v) throws InvariantViolation;
    T content() throws InvariantViolation;
}
