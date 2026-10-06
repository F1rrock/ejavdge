package org.ejavdge.web.spec.header;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.bytes.Concat;
import org.ejavdge.scalar.bytes.Utf8;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.scalar.text.Stencil;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.scalar.text.TextOfNum;
import org.ejavdge.web.spec.Terminator;
/**
 * Header.
 */

public final class Header implements Bytes {
    private final Bytes src;
    /**
     * Creates a new {@code Header}.
     * @param n the 'n' argument
     * @param v the 'v' argument
     */

    public Header(final Text n, final Num v) {
        this(n, new TextOfNum(v));
    }
    /**
     * Creates a new {@code Header}.
     * @param n the 'n' argument
     * @param v the 'v' argument
     */

    public Header(final Text n, final Text v) {
        this(
            new Concat(
                new Utf8(
                    new Stencil(
                        new Text.Of("%s: %s"),
                        n, v
                    )
                ),
                new Terminator()
            )
        );
    }
    /**
     * Creates a new {@code Header}.
     * @param bs the bs
     */

    public Header(final Bytes bs) {
        this.src = bs;
    }
    /**
     * Returns the underlying content.
     * @return the byte
     * @throws InvariantViolation if an invariant is violated
     */

    @Override
    public byte[] content() throws InvariantViolation {
        return this.src.content();
    }
}
