package org.ejavdge.items;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.ContentBased;
import org.ejavdge.scalar.text.Empty;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.scalar.text.Trimmed;

import java.util.List;

/**
 * An {@link Items} view over the "significant" lines of a text.
 * <p>
 * This class extracts the meaningful lines from a given {@link Text}, discarding
 * lines that are empty or contain only whitespace. The processing pipeline is
 * as follows:
 * <ol>
 *   <li>The source text is split into individual lines using {@link Lines}
 *       (which splits on newline characters by default).</li>
 *   <li>Each line is trimmed of leading and trailing whitespace via
 *       {@link Trimmed}.</li>
 *   <li>The trimmed lines are wrapped in {@link ContentBased} so that they can
 *       be compared by their content rather than by object identity.</li>
 *   <li>Lines whose content equals an empty {@link Empty} text are filtered
 *       out using {@link OnlyWhere}.</li>
 * </ol>
 * <p>
 * The resulting collection contains only the non-empty, trimmed lines of the
 * source text, in their original order. This is useful for processing extracted
 * HTML or report text, where blank lines are common and should be ignored when
 * the content is being interpreted line by line.
 */
public final class SignificantLines implements Items<Text> {

    /**
     * The underlying collection of significant lines.
     */
    private final Items<Text> origin;

    /**
     * Creates a collection of significant lines from the given text.
     * <p>
     * The source text is split into lines, each line is trimmed, and lines
     * whose trimmed content is empty are discarded.
     *
     * @param t the source text from which significant lines are extracted
     */
    public SignificantLines(final Text t) {
        this.origin = new OnlyWhere<>(
            l -> !l.equals(new ContentBased(new Empty())),
            new Map<>(
                ContentBased::new,
                new Map<>(
                    Trimmed::new,
                    new Lines(t)
                )
            )
        );
    }

    /**
     * Returns the list of significant (non-empty, trimmed) lines.
     *
     * @return the list of significant lines as text fragments
     * @throws InvariantViolation if an invariant is violated while materializing
     *         the contents
     */
    @Override
    public List<Text> contents() throws InvariantViolation {
        return this.origin.contents();
    }
}
