package org.ejavdge.web.driver.jdk.socket.body;

import org.ejavdge.error.InvariantViolation;

import java.util.function.UnaryOperator;
import java.util.stream.IntStream;

public final class UnsupportedPolicy implements UnaryOperator<IntStream> {
    private final String message;

    public UnsupportedPolicy() {
        this.message = "Unknown response body structure.";
    }

    @Override
    public IntStream apply(final IntStream s) {
        throw new InvariantViolation(this.message);
    }
}
