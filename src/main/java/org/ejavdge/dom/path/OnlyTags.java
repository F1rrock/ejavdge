package org.ejavdge.dom.path;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.items.Items;
import org.ejavdge.items.Map;
import org.ejavdge.items.Populated;
import org.ejavdge.scalar.text.Concat;
import org.ejavdge.scalar.text.NonEmpty;
import org.ejavdge.scalar.text.Stencil;
import org.ejavdge.scalar.text.Text;

public final class OnlyTags implements DocPath {
    private final Text src;

    public OnlyTags(final String ...ss) {
        this(new Map<>(Text.Of::new, new Items.Of<>(ss)));
    }

    public OnlyTags(final Text ...ts) {
        this(new Items.Of<>(ts));
    }

    public OnlyTags(final Items<Text> ts) {
        this(ts, new AllNodes());
    }

    public OnlyTags(final Items<Text> ts, final DocPath p) {
        this.src = new Stencil(
            new Text.Of("%s[%s]"),
            new TextOfPath(p),
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
            )
        );
    }

    @Override
    public String view() throws InvariantViolation {
        return this.src.content();
    }
}
