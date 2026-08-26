package org.ejavdge.dom.path;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.items.Items;
import org.ejavdge.scalar.text.Text;

public final class NestedTag implements DocPath {
    private final DocPath origin;

    public NestedTag(final String s, final DocPath p) {
        this(new Text.Of(s), p);
    }

    public NestedTag(final Text t, final DocPath p) {
        this(new NestedTags(new Items.Of<>(t), p));
    }

    public NestedTag(final DocPath p) {
        this.origin = p;
    }

    @Override
    public String view() throws InvariantViolation {
        return this.origin.view();
    }
}
