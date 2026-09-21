package org.ejavdge.items;

import org.ejavdge.error.InvariantViolation;

import java.util.List;
import java.util.function.Predicate;

public final class OnlyWhere<T> implements Items<T> {
    private final Predicate<T> predicate;
    private final Items<T> origin;

    public OnlyWhere(final Predicate<T> f, final Items<T> xs) {
        this.predicate = f;
        this.origin = xs;
    }

    @Override
    public List<T> contents() throws InvariantViolation {
        return this.origin.contents()
            .stream()
            .filter(this.predicate)
            .toList();
    }
}
