package org.ejavdge.web.spec.header;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.items.Items;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.bytes.Concat;
import org.ejavdge.scalar.bytes.NonEmpty;
import org.ejavdge.web.spec.ByteView;
import org.ejavdge.web.spec.HttpSpec;

public final class WithHeaders implements HttpSpec {
    private final Bytes src;

    public WithHeaders(final Header h, final HttpSpec s) {
        this(new Items.Of<>(h), s);
    }

    public WithHeaders(final Items<Header> hs, final HttpSpec s) {
        this.src = new Concat(
            new NonEmpty(
                new ByteView(s)
            ),
            new Concat(hs)
        );
    }

    @Override
    public byte[] bytes() throws InvariantViolation {
        return this.src.content();
    }
}
