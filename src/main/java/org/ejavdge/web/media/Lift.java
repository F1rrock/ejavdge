package org.ejavdge.web.media;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

public final class Lift<T> implements Media<Media<T>> {
    private final Media<T> src;

    public Lift(final Media<T> m) {
        this.src = m;
    }

    @Override
    public Lift<T> with(final Text n, final Text v) throws InvariantViolation {
        return new Lift<>(this.src.with(n, v));
    }

    @Override
    public Media<T> content() throws InvariantViolation {
        return this.src;
    }
}
