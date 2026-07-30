package org.ejavdge.scalar.text;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.items.Items;
import org.ejavdge.items.Map;

import java.util.stream.Collectors;

public final class Concat implements Text {
    private final Text sep;
    private final Items<? extends Text> xs;

    public Concat(final String ...xs) {
        this(
            new Map<>(
                Text.Of::new,
                new Items.Of<>(xs)
            )
        );
    }

    public Concat(final Text ...xs) {
        this(new Items.Of<>(xs));
    }

    public Concat(final Items<? extends Text> xs) {
        this(new Empty(), xs);
    }

    public Concat(final Text sep, final Items<? extends Text> xs) {
        this.sep = sep;
        this.xs = xs;
    }

    @Override
    public String content() throws InvariantViolation {
        return this.xs.contents()
            .stream()
            .map(Text::content)
            .collect(Collectors.joining(this.sep.content()));
    }
}
