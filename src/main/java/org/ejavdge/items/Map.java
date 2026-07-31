package org.ejavdge.items;

import org.ejavdge.error.InvariantViolation;

import java.util.List;
import java.util.function.Function;

public final class Map<D, R> implements Items<R> {
    private final Function<D, R> mapping;
    private final Items<D> prototype;

    public Map(
        final Function<D, R> f,
        final Items<D> xs
    ) {
        this.mapping = f;
        this.prototype = xs;
    }

    @Override
    public List<R> contents() throws InvariantViolation {
        return this.prototype.contents()
            .stream()
            .map(this.mapping)
            .toList();
    }
}
