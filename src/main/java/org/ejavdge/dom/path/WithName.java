package org.ejavdge.dom.path;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.items.Items;
import org.ejavdge.scalar.text.Text;

public final class WithName implements DocPath {
    private final DocPath origin;

    public WithName(final String s) {
        this(new Text.Of(s));
    }

    public WithName(final Text t) {
        this(
            new WithNames(
                new Items.Of<>(t)
            )
        );
    }

    public WithName(final String s, final DocPath p) {
        this(new Text.Of(s), p);
    }

    public WithName(final Text t, final DocPath p) {
        this(
            new WithNames(
                new Items.Of<>(t),
                p
            )
        );
    }

    public WithName(final DocPath p) {
        this.origin = p;
    }

    @Override
    public String view() throws InvariantViolation {
        return this.origin.view();
    }
}
