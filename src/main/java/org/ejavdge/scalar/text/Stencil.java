package org.ejavdge.scalar.text;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.items.Items;

import java.util.IllegalFormatException;

public final class Stencil implements Text {
    private final Text template;
    private final Items<Text> xs;

    public Stencil(final Text t, final Text ...xs) {
        this(t, new Items.Of<>(xs));
    }

    public Stencil(final Text t, final Items<Text> xs) {
        this.template = t;
        this.xs = xs;
    }

    @Override
    public String content() throws InvariantViolation {
        try {
            return String.format(
                this.template.content(),
                this.xs.contents()
                    .stream()
                    .map(Text::content)
                    .toArray()
            );
        } catch (final IllegalFormatException e) {
            throw new InvariantViolation(
                "Incorrect stencil.\n",
                e
            );
        }
    }
}
