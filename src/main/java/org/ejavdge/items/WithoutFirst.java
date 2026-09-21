package org.ejavdge.items;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.num.NonNegative;
import org.ejavdge.scalar.num.Num;

import java.util.List;

public final class WithoutFirst<T> implements Items<T> {
    private final Num count;
    private final Items<T> origin;

    public WithoutFirst(final Items<T> xs) {
        this(new Num.Of(1), xs);
    }

    public WithoutFirst(final Num n, final Items<T> xs) {
        this.count = new NonNegative(n);
        this.origin = xs;
    }

    @Override
    public List<T> contents() throws InvariantViolation {
        return this.origin.contents()
            .stream()
            .skip(this.count.value())
            .toList();
    }
}
