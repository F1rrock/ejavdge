package org.ejavdge.dom.path;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.items.Items;
import org.ejavdge.items.Map;
import org.ejavdge.items.Populated;
import org.ejavdge.scalar.text.*;

public final class BeforeTags implements DocPath {
    private final Text src;

    public BeforeTags(final Items<Text> ts) {
        this(ts, new AllNodes());
    }

    public BeforeTags(final Items<Text> ts, final DocPath p) {
        this.src = new BindOfText(
            new Concat(
                new Text.Of(" or "),
                new Map<>(
                    t -> new Stencil(
                        new Text.Of(
                            "local-name() = '%s'"
                        ),
                        t
                    ),
                    new Map<>(
                        NonEmpty::new,
                        new Populated<>(ts)
                    )
                )
            ),
            c -> new Stencil(
                new Concat(
                    "%s[following-sibling::*[%s]",
                    " and ",
                    "not(preceding-sibling::*[%s])",
                    " and ",
                    "not(%s)]"
                ),
                new TextOfPath(p),
                new Text.Of(c),
                new Text.Of(c),
                new Text.Of(c)
            )
        );
    }

    @Override
    public String view() throws InvariantViolation {
        return this.src.content();
    }
}
