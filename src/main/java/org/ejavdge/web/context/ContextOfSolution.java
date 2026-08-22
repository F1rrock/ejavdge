package org.ejavdge.web.context;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.web.media.Media;

public final class ContextOfSolution implements Context {
    private final Context origin;

    public ContextOfSolution(final ProbId p, final LangId l) {
        this(new Union(p, l));
    }

    public ContextOfSolution(final Context c) {
        this.origin = c;
    }

    @Override
    public <T> T imprint(Media<T> m) throws InvariantViolation {
        return this.origin.imprint(m);
    }
}
