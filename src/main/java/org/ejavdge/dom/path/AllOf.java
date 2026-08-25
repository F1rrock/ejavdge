package org.ejavdge.dom.path;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.items.Items;
import org.ejavdge.items.Map;
import org.ejavdge.scalar.text.Concat;
import org.ejavdge.scalar.text.Stencil;
import org.ejavdge.scalar.text.Text;

public final class AllOf implements DocPath {
    private final Text src;

    public AllOf(final DocPath ...ps) {
        this(new Items.Of<>(ps));
    }

    public AllOf(final Items<DocPath> ps) {
        this(new Text.Of("\n"), ps);
    }

    public AllOf(final Text t, final DocPath ...ps) {
        this(t, new Items.Of<>(ps));
    }

    public AllOf(final Text t, final Items<DocPath> ps) {
        this.src = new Stencil(
            new Text.Of("string-join((%s), '%s')"),
            new Concat(
                new Text.Of(", "),
                new Map<>(TextOfPath::new, ps)
            ),
            t
        );
    }

    @Override
    public String view() throws InvariantViolation {
        return this.src.content();
    }
}
