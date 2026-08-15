package org.ejavdge.web.media;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.items.Items;
import org.ejavdge.items.Joint;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.web.context.Context;

import java.util.List;

public final class Gist implements Media<List<Text>> {
    private final Items<Text> src;

    public Gist() {
        this(new Items.Of<>());
    }

    public Gist(final Items<Text> src) {
        this.src = src;
    }

    @Override
    public Gist with(Text n, Text v) {
        return new Gist(
            new Joint<>(
                this.src,
                new Items.Of<>(v)
            )
        );
    }

    @Override
    public List<Text> content() throws InvariantViolation {
        return this.src.contents();
    }

    public static final class ImprintOf implements Items<Text> {
        private final Context ctx;
        private final Gist gist;

        public ImprintOf(final Context c) {
            this.ctx = c;
            this.gist = new Gist();
        }

        @Override
        public List<Text> contents() throws InvariantViolation {
            return this.ctx.imprint(this.gist);
        }
    }
}
