package org.ejavdge.web.media;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.items.Items;
import org.ejavdge.items.Map;
import org.ejavdge.scalar.text.ContentBased;
import org.ejavdge.scalar.text.Text;

public final class WhiteList<T> implements Media<T> {
    private final Items<ContentBased> members;
    private final Media<T> origin;

    public WhiteList(final WhiteList<T> l, final Media<T> m) {
        this.members = l.members;
        this.origin = m;
    }

    public WhiteList(final Text x, final Media<T> m) {
        this(new Items.Of<>(x), m);
    }

    public WhiteList(final Items<Text> xs, final Media<T> m) {
        this.members = new Map<>(ContentBased::new, xs);
        this.origin = m;
    }

    @Override
    public WhiteList<T> with(final Text n, final Text v) throws InvariantViolation {
        if (this.members.contents().contains(new ContentBased(n))) {
            return new WhiteList<>(
                this,
                this.origin.with(n, v)
            );
        }
        return this;
    }

    @Override
    public T content() throws InvariantViolation {
        return this.origin.content();
    }
}
