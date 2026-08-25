package org.ejavdge.dom.path;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.items.Items;
import org.ejavdge.scalar.text.Text;

public final class OnlyTag implements DocPath {
    private final DocPath origin;

    public OnlyTag(final String s) {
        this(new Text.Of(s));
    }

    public OnlyTag(final Text t) {
        this(
            new OnlyTags(
                new Items.Of<>(t)
            )
        );
    }

    public OnlyTag(final String s, final DocPath p) {
        this(new Text.Of(s), p);
    }

    public OnlyTag(final Text t, final DocPath p) {
        this(
            new OnlyTags(
                new Items.Of<>(t),
                p
            )
        );
    }

    public OnlyTag(final DocPath p) {
        this.origin = p;
    }

    @Override
    public String view() throws InvariantViolation {
        return this.origin.view();
    }
}
