package org.ejavdge.dom.path;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.items.Items;
import org.ejavdge.items.Map;
import org.ejavdge.items.Populated;
import org.ejavdge.scalar.text.BindOfText;
import org.ejavdge.scalar.text.Concat;
import org.ejavdge.scalar.text.Stencil;
import org.ejavdge.scalar.text.Text;

public final class AfterClasses implements DocPath {
    private final Text src;

    public AfterClasses(final Items<Text> ts) {
        this(ts, new AllNodes());
    }

    public AfterClasses(final Items<Text> ts, final DocPath p) {
        this.src = new BindOfText(
            new Concat(
                new Text.Of(" or "),
                new Map<>(
                    t -> new Stencil(
                        new Text.Of(
                            "contains(concat(' ', normalize-space(@class), ' '), ' %s ')"
                        ),
                        t
                    ),
                    new Populated<>(ts)
                )
            ),
            c -> new Stencil(
                new Concat(
                    "%s[preceding-sibling::*[%s]"
                            + " and "
                            + "not(following-sibling::*[%s])"
                            + " and "
                            + "not(%s)"
                            + "]"
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
