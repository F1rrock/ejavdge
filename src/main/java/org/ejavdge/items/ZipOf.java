package org.ejavdge.items;

import org.ejavdge.error.InvariantViolation;

import java.util.List;

public final class ZipOf<T> implements Items<Items<T>> {
    private final Items<Items<T>> src;

    @SafeVarargs
    public ZipOf(final Items<T> ...xss) {
        this(new Items.Of<>(xss));
    }

    public ZipOf(final Items<Items<T>> xss) {
        this.src = xss;
    }

    @Override
    public List<Items<T>> contents() throws InvariantViolation {
        final var head = new Joint<>(
            new Map<>(
                OnlyFirst::new,
                this.src
            )
        ).contents();
        if (head.isEmpty()) {
            return List.of();
        }
        return new Joint<>(
            new Items.Of<>(
                new Items.Of<>(head)
            ),
            new ZipOf<>(
                new Map<>(
                    WithoutFirst::new,
                    this.src
                )
            )
        ).contents();
    }
}
