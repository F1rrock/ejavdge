package org.ejavdge.web.driver.jdk.socket.body;

import org.ejavdge.scalar.num.Num;

import java.util.function.UnaryOperator;
import java.util.stream.IntStream;

public final class WithLimitation implements UnaryOperator<IntStream> {
    final Num size;

    public WithLimitation(final Num n) {
        this.size = n;
    }

    @Override
    public IntStream apply(final IntStream s) {
        return s.limit(this.size.value());
    }
}
