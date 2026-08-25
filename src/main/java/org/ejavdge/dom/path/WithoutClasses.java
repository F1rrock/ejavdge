package org.ejavdge.dom.path;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.items.Items;
import org.ejavdge.items.Map;
import org.ejavdge.items.Populated;
import org.ejavdge.scalar.text.Concat;
import org.ejavdge.scalar.text.Stencil;
import org.ejavdge.scalar.text.Text;

public final class WithoutClasses implements DocPath {
    private final Text src;

    public WithoutClasses(final Items<Text> ts) {
        this(ts, new AllNodes());
    }

    public WithoutClasses(final Items<Text> ts, final DocPath p) {
        this.src = new Stencil(
            new Text.Of("%s[not(%s)]"),
            new TextOfPath(p),
            new Concat(
                new Text.Of(" or "),
                new Map<>(
                    t -> new Stencil(
                        new Text.Of(
                            "@class = '%s'"
                        ),
                        t
                    ),
                    new Populated<>(ts)
                )
            )
        );
    }

    @Override
    public String view() throws InvariantViolation {
        return this.src.content();
    }
}
