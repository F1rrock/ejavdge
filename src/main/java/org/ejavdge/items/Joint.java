package org.ejavdge.items;

import org.ejavdge.error.InvariantViolation;

import java.util.List;

public final class Joint<T> implements Items<T> {
    private final Items<Items<T>> xss;

    @SafeVarargs
    public Joint(final Items<T> ...xss) {
        this(new Items.Of<>(xss));
    }

    public Joint(final Items<Items<T>> xss) {
        this.xss = xss;
    }

    @Override
    public List<T> contents() throws InvariantViolation {
        return this.xss.contents()
                .stream()
                .flatMap(xs -> xs.contents().stream())
                .toList();
    }
}
