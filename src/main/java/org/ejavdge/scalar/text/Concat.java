package org.ejavdge.scalar.text;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.items.Items;
import org.ejavdge.items.Map;

import java.util.stream.Collectors;

/**
 * A {@link Text} that concatenates the contents of several text fragments,
 * optionally separated by a delimiter.
 * <p>
 * This class implements {@link Text} and represents the concatenation of a
 * collection of text values. When {@link #content()} is called, each constituent
 * text is materialized via {@link Text#content()}, and the resulting strings are
 * joined together in order. If a separator is provided, it is inserted between
 * consecutive fragments; otherwise, an {@link Empty} separator is used, so the
 * fragments are joined with no delimiter.
 * <p>
 * The fragments can be supplied in several ways:
 * <ul>
 *   <li>as varargs of plain {@link String} values, which are wrapped into
 *       {@link Text.Of} instances;</li>
 *   <li>as varargs of {@link Text} values;</li>
 *   <li>as an {@link Items} collection of text values (using a wildcard type to
 *       allow any {@code Text} subtype);</li>
 *   <li>as an {@link Items} collection together with an explicit separator.</li>
 * </ul>
 * <p>
 * This is a fundamental building block for assembling composite text values in
 * a declarative manner, such as building a report body from multiple sections or
 * joining lines extracted from a document.
 */
public final class Concat implements Text {

    /**
     * The separator inserted between consecutive text fragments.
     */
    private final Text sep;

    /**
     * The collection of text fragments to concatenate.
     */
    private final Items<? extends Text> xs;

    /**
     * Creates a concatenation of the given strings, using an empty separator.
     *
     * @param xs the strings to concatenate
     */
    public Concat(final String ...xs) {
        this(
            new Map<>(
                Text.Of::new,
                new Items.Of<>(xs)
            )
        );
    }

    /**
     * Creates a concatenation of the given text fragments, using an empty
     * separator.
     *
     * @param xs the text fragments to concatenate
     */
    public Concat(final Text ...xs) {
        this(new Items.Of<>(xs));
    }

    /**
     * Creates a concatenation of the given collection of text fragments, using
     * an empty separator.
     *
     * @param xs the collection of text fragments to concatenate
     */
    public Concat(final Items<? extends Text> xs) {
        this(new Empty(), xs);
    }

    /**
     * Creates a concatenation of the given collection of text fragments, using
     * the specified separator.
     *
     * @param sep the separator inserted between consecutive fragments
     * @param xs  the collection of text fragments to concatenate
     */
    public Concat(final Text sep, final Items<? extends Text> xs) {
        this.sep = sep;
        this.xs = xs;
    }

    /**
     * Returns the concatenated content of all text fragments.
     * <p>
     * Each fragment is materialized in the order it was provided, and the
     * resulting strings are joined together using the separator's content as
     * the delimiter. If any fragment fails to produce its content, the
     * corresponding {@link InvariantViolation} is propagated.
     *
     * @return the concatenated text as a single string
     * @throws InvariantViolation if any constituent text or the separator
     *         violates an invariant while materializing its content
     */
    @Override
    public String content() throws InvariantViolation {
        return this.xs.contents()
            .stream()
            .map(Text::content)
            .collect(Collectors.joining(this.sep.content()));
    }
}
