package org.ejavdge.items;

import org.ejavdge.error.InvariantViolation;

import java.util.List;

public final class Populated<T> implements Items<T> {
    private final Items<T> origin;

    public Populated(final Items<T> xs) {
        this.origin = xs;
    }

    @Override
    public List<T> contents() throws InvariantViolation {
        final var list = this.origin.contents();
        if (list.isEmpty()) {
            throw new InvariantViolation("Items is not populated");
        }
        return list;
    }
}
