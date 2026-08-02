package org.ejavdge.web.media;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.items.Items;
import org.ejavdge.items.Joint;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.bytes.Utf8;
import org.ejavdge.scalar.text.Concat;
import org.ejavdge.scalar.text.Stencil;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.scalar.text.UrlText;
import org.ejavdge.web.context.Context;

public final class Form implements Media<byte[]> {
    private final Items<Text> src;

    public Form() {
        this(new Items.Of<>());
    }

    public Form(final Items<Text> src) {
        this.src = src;
    }

    @Override
    public Form with(final Text n, final Text v) {
        return new Form(
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
    public byte[] content() throws InvariantViolation {
        return new Utf8(
            new Concat(
                new Text.Of("&"),
                this.src
            )
        ).content();
    }

    public static final class ImprintOf implements Bytes {
        private final Context ctx;
        private final Form form;

        public ImprintOf(final Context c) {
            this.ctx = c;
            this.form = new Form();
        }

        @Override
        public byte[] content() throws InvariantViolation {
            return this.ctx.imprint(this.form);
        }
    }
}
