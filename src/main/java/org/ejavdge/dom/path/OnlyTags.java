package org.ejavdge.dom.path;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.items.Items;
import org.ejavdge.items.Map;
import org.ejavdge.items.Populated;
import org.ejavdge.scalar.text.Concat;
import org.ejavdge.scalar.text.NonEmpty;
import org.ejavdge.scalar.text.Stencil;
import org.ejavdge.scalar.text.Text;

/**
 * A {@link DocPath} that restricts a selection to elements whose local name
 * matches any of the specified tag names.
 * <p>
 * The resulting XPath expression is of the form
 * {@code <base>[local-name() = '<tag1>' or local-name() = '<tag2>' or ...]},
 * where {@code <base>} is the string representation of the base path. The
 * condition matches elements whose local name equals one of the given tags.
 * Tag names are required to be non-empty.
 * <p>
 * When no base path is provided, {@link AllNodes} is used, so the tag filter
 * is applied to the entire document. This class also provides convenience
 * constructors that accept tag names as plain strings or as {@link Text}
 * varargs.
 */
public final class OnlyTags implements DocPath {

    /**
     * The XPath expression produced by this path.
     */
    private final Text src;

    /**
     * Creates a path that matches elements with any of the given tag names,
     * searching across all nodes.
     *
     * @param ss the tag names to match
     */
    public OnlyTags(final String ...ss) {
        this(new Map<>(Text.Of::new, new Items.Of<>(ss)));
    }

    /**
     * Creates a path that matches elements with any of the given tag names,
     * searching across all nodes.
     *
     * @param ts the tag names to match, as text
     */
    public OnlyTags(final Text ...ts) {
        this(new Items.Of<>(ts));
    }

    /**
     * Creates a path that matches elements with any of the given tag names,
     * searching across all nodes.
     *
     * @param ts the tag names to match
     */
    public OnlyTags(final Items<Text> ts) {
        this(ts, new AllNodes());
    }

    /**
     * Creates a path that matches elements with any of the given tag names,
     * constrained to the nodes matched by the given base path.
     * <p>
     * The generated XPath expression is
     * {@code <base>[local-name() = '<tag1>' or local-name() = '<tag2>' or ...]},
     * where {@code <base>} is the view of the base path. Each non-empty tag
     * contributes a {@code local-name() = '<tag>'} predicate, and the
     * predicates are combined with {@code or}.
     *
     * @param ts the tag names to match
     * @param p  the base path constraining the search
     */
    public OnlyTags(final Items<Text> ts, final DocPath p) {
        this.src = new Stencil(
            new Text.Of("%s[%s]"),
            new TextOfPath(p),
            new Concat(
                new Text.Of(" or "),
                new Map<>(
                    t -> new Stencil(
                        new Text.Of(
                            "local-name() = '%s'"
                        ),
                        t
                    ),
                    new Map<>(
                        NonEmpty::new,
                        new Populated<>(ts)
                    )
                )
            )
        );
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
        return this.src.content();
    }
}
