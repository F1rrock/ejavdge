package org.ejavdge.domain.run;

import org.ejavdge.dom.path.*;
import org.ejavdge.error.InvariantViolation;

public final class PathToTableRows implements DocPath {
    private final DocPath origin;

    public PathToTableRows() {
        this.origin = new NestedTag(
            "tr",
            new WithClass(
                "table",
                new NestedTag(
                    "table",
                    new WithId(
                        "ej-main-submit-tab",
                        new OnlyTag("div")
                    )
                )
            )
        );
    }

    public PathToTableRows(final DocPath p) {
        this.origin = p;
    }

    @Override
    public String view() throws InvariantViolation {
        return this.origin.view();
    }
}
