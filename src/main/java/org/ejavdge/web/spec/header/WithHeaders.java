package org.ejavdge.web.spec.header;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.items.Items;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.bytes.Concat;
import org.ejavdge.scalar.bytes.NonEmpty;
import org.ejavdge.web.spec.ByteView;
import org.ejavdge.web.spec.HttpSpec;
/**
 * With headers.
 */

public final class WithHeaders implements HttpSpec {
    private final Bytes src;
    /**
     * Creates a new {@code WithHeaders}.
     * @param h the 'h' argument
     * @param s the 's' argument
     */

    public WithHeaders(final Header h, final HttpSpec s) {
        this(new Items.Of<>(h), s);
    }
    /**
     * Creates a new {@code WithHeaders}.
     * @param hs the hs
     * @param s the 's' argument
     */

    public WithHeaders(final Items<Header> hs, final HttpSpec s) {
        this.src = new Concat(
            new NonEmpty(
                new ByteView(s)
            ),
            new Concat(hs)
        );
    }
    /**
     * Returns the HTTP message as bytes.
     * @return the byte
     * @throws InvariantViolation if an invariant is violated
     */

    @Override
    public byte[] bytes() throws InvariantViolation {
        return this.src.content();
    }
}
