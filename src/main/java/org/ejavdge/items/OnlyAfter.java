package org.ejavdge.items;

import org.ejavdge.error.InvariantViolation;

import java.util.List;
import java.util.function.Predicate;

public final class OnlyAfter<T> implements Items<T> {
    private final Items<T> origin;
    private final Predicate<T> predicate;

    public OnlyAfter(final Predicate<T> f, final Items<T> xs) {
        this.predicate = f;
        this.origin = xs;
    }

    @Override
    public List<T> contents() throws InvariantViolation {
        return this.origin.contents()
            .stream()
            .dropWhile(this.predicate.negate())
            .skip(1)
            .toList();
    }
}
