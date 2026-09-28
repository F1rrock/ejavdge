package org.ejavdge.dom.path;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.items.Items;
import org.ejavdge.scalar.text.Text;

public final class BeforeTag implements DocPath {
    private final DocPath origin;

    public BeforeTag(final String s) {
        this(new Text.Of(s));
    }

    public BeforeTag(final Text t) {
        this(
            new BeforeTags(
                new Items.Of<>(t)
            )
        );
    }

    public BeforeTag(final String s, final DocPath p) {
        this(new Text.Of(s), p);
    }

    public BeforeTag(final Text t, final DocPath p) {
        this(
            new BeforeTags(
                new Items.Of<>(t),
                p
            )
        );
    }

    public BeforeTag(final DocPath p) {
        this.origin = p;
    }

    @Override
    public String view() throws InvariantViolation {
        return this.origin.view();
    }
}
