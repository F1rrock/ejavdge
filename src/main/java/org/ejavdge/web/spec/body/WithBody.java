package org.ejavdge.web.spec.body;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.*;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.web.spec.ByteView;
import org.ejavdge.web.spec.HttpSpec;
import org.ejavdge.web.spec.Terminator;
import org.ejavdge.web.spec.header.Header;
import org.ejavdge.web.spec.header.WithHeaders;
/**
 * With body.
 */

public final class WithBody implements HttpSpec {
    private final Bytes src;
    /**
     * Creates a new {@code WithBody}.
     * @param b the 'b' argument
     * @param s the 's' argument
     */

    public WithBody(final Bytes b, final HttpSpec s) {
        final var body = new Memo(b);
        this.src = new Concat(
            new NonEmpty(
                new ByteView(
                    new WithHeaders(
                        new Header(
                            new Text.Of("Content-Length"),
                            new Size(body)
                        ),
                        s
                    )
                )
            ),
            new Terminator(),
            body
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
