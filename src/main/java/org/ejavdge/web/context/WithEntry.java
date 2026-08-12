package org.ejavdge.web.context;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.web.media.Media;

public final class WithEntry implements Context {
    private final Text name;
    private final Text value;
    private final Context origin;

    public WithEntry(final Text n, final Text v) {
        this(n, v, new NoContext());
    }

    public WithEntry(final Text n, final Text v, final Context c) {
        this.name = n;
        this.value = v;
        this.origin = c;
    }

    @Override
    public <T> T imprint(final Media<T> m) throws InvariantViolation {
        return this.origin.imprint(m.with(this.name, this.value));
    }
}
