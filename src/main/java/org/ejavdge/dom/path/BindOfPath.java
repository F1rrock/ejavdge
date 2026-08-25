package org.ejavdge.dom.path;

import org.ejavdge.error.InvariantViolation;

import java.util.function.Function;

public final class BindOfPath implements DocPath {
    private final DocPath origin;
    private final Function<String, DocPath> binding;

    public BindOfPath(final DocPath p, final Function<String, DocPath> f) {
        this.origin = p;
        this.binding = f;
    }

    @Override
    public String view() throws InvariantViolation {
        return this.binding.apply(
            this.origin.view()
        ).view();
    }
}
