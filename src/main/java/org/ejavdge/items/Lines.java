package org.ejavdge.items;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

import java.util.List;

public final class Lines implements Items<Text> {
    private final Text sep;
    private final Text src;

    public Lines(final Text t) {
        this(new Text.Of("\n"), t);
    }

    public Lines(final Text sep, final Text src) {
        this.sep = sep;
        this.src = src;
    }

    @Override
    public List<Text> contents() throws InvariantViolation {
        return new Map<String, Text>(
            Text.Of::new,
            new Items.Of<>(
                this.src.content().split(this.sep.content())
            )
        ).contents();
    }
}
