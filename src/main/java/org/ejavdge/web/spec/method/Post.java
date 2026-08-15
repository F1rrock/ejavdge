package org.ejavdge.web.spec.method;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.items.Map;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.bytes.Utf8;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.scalar.text.NonEmpty;
import org.ejavdge.scalar.text.Stencil;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.web.context.Location;
import org.ejavdge.web.media.Gist;
import org.ejavdge.web.spec.HttpSpec;

public final class Post implements HttpSpec {
    private final Bytes src;

    public Post(final Text u, final Text h, final Num p) {
        this(new Location(u, h, p));
    }

    public Post(final Location loc) {
        this.src = new Utf8(
            new Stencil(
                new Text.Of(
                    """
                    POST %s HTTP/1.1\r
                    Host: %s:%s\r
                    """
                ),
                new Map<>(
                    NonEmpty::new,
                    new Gist.ImprintOf(loc)
                )
            )
        );
    }

    @Override
    public byte[] bytes() throws InvariantViolation {
        return this.src.content();
    }
}
