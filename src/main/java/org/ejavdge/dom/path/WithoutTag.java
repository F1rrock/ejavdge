package org.ejavdge.dom.path;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.items.Items;
import org.ejavdge.scalar.text.Text;

public final class WithoutTag implements DocPath {
    private final DocPath origin;

    public WithoutTag(final String s) {
        this(new Text.Of(s));
    }

    public WithoutTag(final Text t) {
        this(
            new WithoutTags(
                new Items.Of<>(t)
            )
        );
    }

    public WithoutTag(final String s, final DocPath p) {
        this(new Text.Of(s), p);
    }

    public WithoutTag(final Text t, final DocPath p) {
        this(
            new WithoutTags(
                new Items.Of<>(t),
                p
            )
        );
    }

    public WithoutTag(final DocPath p) {
        this.origin = p;
    }

    @Override
    public String view() throws InvariantViolation {
        return this.origin.view();
    }
}
