package org.ejavdge.items;

import org.ejavdge.error.InvariantViolation;

import java.util.List;
import java.util.function.Predicate;

public final class Split<T> implements Items<Items<T>> {
    private final Predicate<T> predicate;
    private final Items<T> src;

    public Split(final Predicate<T> f, final Items<T> xs) {
        this.predicate = f;
        this.src = xs;
    }

    @Override
    public List<Items<T>> contents() throws InvariantViolation {
        final var xs = this.src.contents();
        if (xs.isEmpty()) {
            return List.of();
        }
        return new Joint<>(
            new Items.Of<>(
                new OnlyUntil<>(
                    this.predicate,
                    new Items.Of<>(xs)
                )
            ),
            new Split<>(
                this.predicate,
                new OnlyAfter<>(
                    this.predicate,
                    new Items.Of<>(xs)
                )
            )
        ).contents();
    }
}
