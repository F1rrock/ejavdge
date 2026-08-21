package org.ejavdge.web.spec.body.multipart;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.items.Items;
import org.ejavdge.items.Joint;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.web.context.Context;
import org.ejavdge.web.media.Media;

import java.util.List;

public final class TextParts implements Media<List<Part>> {
    private final Items<Part> src;

    public TextParts() {
        this(new Items.Of<>());
    }

    public TextParts(final Items<Part> ps) {
        this.src = ps;
    }

    @Override
    public TextParts with(final Text n, final Text v) throws InvariantViolation {
        return new TextParts(
            new Joint<>(
                this.src,
                new Items.Of<>(
                    new TextPart(n, v)
                )
            )
        );
    }

    @Override
    public List<Part> content() throws InvariantViolation {
        return this.src.contents();
    }

    public static final class ImprintOf implements Items<Part> {
        private final Context ctx;
        private final TextParts ps;

        public ImprintOf(final Context c) {
            this.ctx = c;
            this.ps = new TextParts();
        }

        @Override
        public List<Part> contents() throws InvariantViolation {
            return this.ctx.imprint(this.ps);
        }
    }
}
