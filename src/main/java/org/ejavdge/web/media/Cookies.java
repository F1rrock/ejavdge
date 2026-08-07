package org.ejavdge.web.media;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.items.Items;
import org.ejavdge.items.Joint;
import org.ejavdge.scalar.text.Concat;
import org.ejavdge.scalar.text.Stencil;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.scalar.text.UrlText;
import org.ejavdge.web.context.Context;

public final class Cookies implements Media<String> {
    private final Items<Text> src;

    public Cookies() {
        this(new Items.Of<>());
    }

    public Cookies(final Items<Text> src) {
        this.src = src;
    }

    @Override
    public Cookies with(final Text n, final Text v) {
        return new Cookies(
            new Joint<>(
                this.src,
                new Items.Of<>(
                    new Stencil(
                        new Text.Of("%s=%s"),
                        new UrlText(n),
                        new UrlText(v)
                    )
                )
            )
        );
    }

    @Override
    public String content() throws InvariantViolation {
        return new Concat(
            new Text.Of("; "),
            this.src
        ).content();
    }

    public static final class ImprintOf implements Text {
        private final Context ctx;
        private final Cookies cookies;

        public ImprintOf(final Context c) {
            this.ctx = c;
            this.cookies = new Cookies();
        }

        @Override
        public String content() throws InvariantViolation {
            return this.ctx.imprint(this.cookies);
        }
    }
}
