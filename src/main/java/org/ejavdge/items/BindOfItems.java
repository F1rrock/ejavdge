package org.ejavdge.items;

import org.ejavdge.error.InvariantViolation;

import java.util.List;
import java.util.function.Function;

public final class BindOfItems<T> implements Items<T> {
    private final Items<T> origin;
    private final Function<List<T>, Items<T>> binding;

    public BindOfItems(final Items<T> xs, final Function<List<T>, Items<T>> f) {
        this.origin = xs;
        this.binding = f;
    }

    @Override
    public List<T> contents() throws InvariantViolation {
        return this.binding.apply(
            this.origin.contents()
        ).contents();
    }
}
