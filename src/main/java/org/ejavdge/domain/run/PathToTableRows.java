package org.ejavdge.domain.run;

import org.ejavdge.dom.path.*;
import org.ejavdge.error.InvariantViolation;

/**
 * A {@link DocPath} that selects the rows of the run table on a problem page.
 * <p>
 * The run table on a problem page is located inside a specific structure:
 * a {@code <div>} with {@code id="ej-main-submit-tab"}, containing a
 * {@code <table>} with CSS class {@code "table"}, which in turn contains a
 * nested {@code <table>}. This path selects all {@code <tr>} elements within
 * that nested table, i.e. the individual rows of the run table.
 * <p>
 * The generated XPath expression corresponds to:
 * {@code //div[@id='ej-main-submit-tab']//table[@class='table']//table//tr}
 * (with {@code NestedTag} semantics applied step by step).
 * <p>
 * A {@code PathToTableRows} can also be created by wrapping an existing
 * {@link DocPath}, in which case it simply delegates to that path.
 */
public final class PathToTableRows implements DocPath {

    /**
     * The underlying document path.
     */
    private final DocPath origin;

    /**
     * Creates a path that selects the rows of the run table on a problem page,
     * using the standard structure described in the class documentation.
     */
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

    /**
     * Creates a path that wraps the given document path directly.
     *
     * @param p the underlying document path
     */
    public PathToTableRows(final DocPath p) {
        this.origin = p;
    }

    /**
     * Returns the XPath expression represented by this path.
     *
     * @return the XPath expression as a string
     * @throws InvariantViolation if an invariant is violated while building the
     *         expression
     */
    @Override
    public String view() throws InvariantViolation {
        return this.origin.view();
    }
}
