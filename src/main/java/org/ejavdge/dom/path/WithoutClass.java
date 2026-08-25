package org.ejavdge.dom.path;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.items.Items;
import org.ejavdge.scalar.text.Text;

public final class WithoutClass implements DocPath {
    private final DocPath origin;

    public WithoutClass(final String s) {
        this(new Text.Of(s));
    }

    public WithoutClass(final Text t) {
        this(
            new WithoutClasses(
                new Items.Of<>(t)
            )
        );
    }

    public WithoutClass(final String s, final DocPath p) {
        this(new Text.Of(s), p);
    }

    public WithoutClass(final Text t, final DocPath p) {
        this(
            new WithoutClasses(
                new Items.Of<>(t),
                p
            )
        );
    }

    public WithoutClass(final DocPath p) {
        this.origin = p;
    }

    @Override
    public String view() throws InvariantViolation {
        return this.origin.view();
    }
}
