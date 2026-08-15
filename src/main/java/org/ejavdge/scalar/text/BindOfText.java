package org.ejavdge.scalar.text;

import org.ejavdge.error.InvariantViolation;

import java.util.function.Function;

public final class BindOfText implements Text {
    private final Text origin;
    private final Function<String, Text> binding;

    public BindOfText(final Text t, final Function<String, Text> f) {
        this.origin = t;
        this.binding = f;
    }

    @Override
    public String content() throws InvariantViolation {
        return this.binding.apply(
            this.origin.content()
        ).content();
    }
}
