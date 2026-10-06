package org.ejavdge.items;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

import java.util.List;

/**
 * An {@link Items} view over the individual lines of a text.
 * <p>
 * This class splits a given {@link Text} into a collection of {@link Text}
 * fragments, one per line, using a configurable separator. By default, the
 * separator is a newline character ({@code "\n"}), which makes this class
 * convenient for working with multi-line text, such as the extracted brief of a
 * problem or a report section.
 * <p>
 * The splitting is performed lazily when {@link #contents()} is called, using
 * {@link String#split(String)} with the separator's content as the delimiter.
 * Each resulting string fragment is wrapped in a {@link Text.Of}.
 */
public final class Lines implements Items<Text> {

    /**
     * The separator used to split the source text into lines.
     */
    private final Text sep;

    /**
     * The source text whose lines are exposed.
     */
    private final Text src;

    /**
     * Creates a collection of lines by splitting the given text on newline
     * characters.
     *
     * @param t the text to split into lines
     */
    public Lines(final Text t) {
        this(new Text.Of("\n"), t);
    }

    /**
     * Creates a collection of lines by splitting the given text on the
     * specified separator.
     *
     * @param sep the separator used to split the source text
     * @param src the text to split into lines
     */
    public Lines(final Text sep, final Text src) {
        this.sep = sep;
        this.src = src;
    }

    /**
     * Returns the list of lines obtained by splitting the source text.
     * <p>
     * The source text's content is split using the separator's content as the
     * delimiter, and each resulting fragment is wrapped in a {@link Text.Of}.
     *
     * @return the list of lines as text fragments
     * @throws InvariantViolation if an invariant is violated while retrieving
     *         the source text or the separator
     */
    @Override
    public List<Text> contents() throws InvariantViolation {
        return new Map<String, Text>(
            Text.Of::new,
            new Items.Of<>(
                this.src.content().split(this.sep.content())
            )
        ).contents();
    }
}
