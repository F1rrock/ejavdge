package org.ejavdge.dom.path;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.items.Items;
import org.ejavdge.scalar.text.Text;

public final class WithClass implements DocPath {
    private final DocPath origin;

    public WithClass(final String s) {
        this(new Text.Of(s));
    }

    public WithClass(final Text t) {
        this(
            new WithClasses(
                new Items.Of<>(t)
            )
        );
    }

    public WithClass(final String s, final DocPath p) {
        this(new Text.Of(s), p);
    }

    public WithClass(final Text t, final DocPath p) {
        this(
            new WithClasses(
                new Items.Of<>(t),
                p
            )
        );
    }

    public WithClass(final DocPath p) {
        this.origin = p;
    }

    @Override
    public String view() throws InvariantViolation {
        return this.origin.view();
    }
}
