package org.ejavdge.domain.solution;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.file.ByteFile;
import org.ejavdge.file.ContentOf;
import org.ejavdge.items.Items;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.scalar.num.NumAbout;
import org.ejavdge.scalar.num.NumOfText;
import org.ejavdge.scalar.num.Positive;
import org.ejavdge.scalar.text.*;

/**
 * A numeric value representing the language index of a solution, extracted from
 * a special comment marker in the solution file.
 * <p>
 * The ejudge submission system requires the author of a solution to specify
 * which compiler/language should be used. This class reads a marker comment of
 * the form {@code // language: <INDEX_IN_SELECTOR>} from the solution file and
 * extracts the numeric index from it.
 * <p>
 * The marker is searched for using the regular expression
 * {@code (?m)(?<=^//\s{0,20}language:\s{0,20})\d+}, which matches a line
 * beginning with {@code //}, followed by the word {@code language:}, optional
 * whitespace, and a sequence of digits. The extracted value is required to be
 * non-empty, positive, and is labelled as "language marker" via
 * {@link NumAbout}.
 * <p>
 * If the marker is missing or malformed, an {@link InvariantViolation} is
 * thrown with a message explaining that a comment such as
 * {@code // language: <INDEX_IN_SELECTOR>} is required.
 * <p>
 * An instance can also be created by wrapping an existing {@link Num}, in which
 * case it simply delegates to that number.
 */
public final class LangIndexOf implements Num {

    /**
     * The underlying numeric value representing the language index.
     */
    private final Num origin;

    /**
     * Creates a language index by extracting the marker comment from the given
     * solution file.
     * <p>
     * The solution file is read as UTF-8 text, and the marker comment is
     * located using the regular expression described in the class
     * documentation. The captured digits are converted to a positive integer
     * and labelled as "language marker".
     * <p>
     * If no marker is found, an {@link InvariantViolation} is thrown with a
     * message indicating that a comment of the form
     * {@code // language: <INDEX_IN_SELECTOR>} is required.
     *
     * @param f the solution file from which the language marker is extracted
     */
    public LangIndexOf(final ByteFile f) {
        this(
            new NumAbout(
                "language marker",
                new Positive(
                    new NumOfText(
                        new NonEmpty(
                            new Match(
                                new Utf8Text(
                                    new ContentOf(f)
                                ),
                                new Text.Of(
                                    "(?m)(?<=^//\\s{0,20}language:\\s{0,20})\\d+"
                                ),
                                new Concat(
                                    new Text.Of(" "),
                                    new Items.Of<>(
                                        new Text.Of(
                                            "There is no language marker"
                                        ),
                                        new Text.Of(
                                            "(comment like `// language: <INDEX_IN_SELECTOR>` is required)."
                                        )
                                    )
                                )
                            )
                        )
                    )
                )
            )
        );
    }

    /**
     * Creates a language index by wrapping an existing numeric value.
     *
     * @param n the numeric value to wrap
     */
    public LangIndexOf(final Num n) {
        this.origin = n;
    }

    /**
     * Returns the numeric value of this language index.
     *
     * @return the language index as an integer
     * @throws InvariantViolation if an invariant is violated while retrieving
     *         the value, or if the language marker is missing or malformed
     */
    @Override
    public int value() throws InvariantViolation {
        return this.origin.value();
    }
}
