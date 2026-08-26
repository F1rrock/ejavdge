package org.ejavdge.dom.path;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.items.Items;
import org.ejavdge.scalar.text.Text;

public final class NestedTags implements DocPath {
    private final DocPath origin;

    public NestedTags(final Items<Text> ts, final DocPath p) {
        this(
            new OnlyTags(
                ts,
                new ChildrenOf(p)
            )
        );
    }

    public NestedTags(final DocPath p) {
        this.origin = p;
    }

    @Override
    public String view() throws InvariantViolation {
        return this.origin.view();
    }
}
