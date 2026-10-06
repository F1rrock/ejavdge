package org.ejavdge.domain.solution;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.file.ByteFile;
import org.ejavdge.file.ContentOf;
import org.ejavdge.items.Items;
import org.ejavdge.scalar.text.*;

/**
 * A textual value representing the short name of a problem, extracted from a
 * special comment marker in a solution file.
 * <p>
 * The ejudge submission system requires the author of a solution to specify
 * which problem the solution is intended for. This class reads a marker comment
 * of the form {@code // problem: <SHORT_NAME>} from the solution file and
 * extracts the problem's short name from it.
 * <p>
 * The marker is searched for using the regular expression
 * {@code (?m)(?<=^//\s{0,20}problem:\s{0,20})[A-Za-z][A-Za-z0-9]*}, which
 * matches a line beginning with {@code //}, followed by the word
 * {@code problem:}, optional whitespace, and an identifier starting with a
 * letter and consisting of letters and digits. The extracted value is required
 * to be non-empty and is labelled as "problem's marker".
 * <p>
 * If the marker is missing or malformed, an {@link InvariantViolation} is
 * thrown with a message explaining that a comment such as
 * {@code // problem: <SHORT_NAME>} is required.
 * <p>
 * An instance can also be created by wrapping an existing {@link Text}.
 */
public final class ProbNameOf implements Text {

    /**
     * The underlying textual content representing the problem name.
     */
    private final Text origin;

    /**
     * Creates a problem name by extracting the marker comment from the given
     * solution file.
     * <p>
     * The solution file is read as UTF-8 text, and the marker comment is
     * located using the regular expression described in the class
     * documentation. The captured name is required to be non-empty and is
     * labelled as "problem's marker".
     * <p>
     * If no marker is found, an {@link InvariantViolation} is thrown with a
     * message indicating that a comment of the form
     * {@code // problem: <SHORT_NAME>} is required.
     *
     * @param f the solution file from which the problem marker is extracted
     */
    public ProbNameOf(final ByteFile f) {
        this(
            new TextAbout(
                "problem's marker",
                new NonEmpty(
                    new Match(
                        new Utf8Text(
                            new ContentOf(f)
                        ),
                        new Text.Of(
                            "(?m)(?<=^//\\s{0,20}problem:\\s{0,20})[A-Za-z][A-Za-z0-9]*"
                        ),
                        new Concat(
                            new Text.Of(" "),
                            new Items.Of<>(
                                new Text.Of(
                                    "There is no problem marker"
                                ),
                                new Text.Of(
                                    "(comment like `// problem: <SHORT_NAME>` is required)."
                                )
                            )
                        )
                    ),
                    new Text.Of("There is empty problem marker.")
                )
            )
        );
    }

    /**
     * Creates a problem name by wrapping an existing text.
     *
     * @param t the text that will become the problem name content
     */
    public ProbNameOf(final Text t) {
        this.origin = t;
    }

    /**
     * Returns the textual content of the problem name.
     *
     * @return the problem name as a string
     * @throws InvariantViolation if an invariant is violated while retrieving
     *         the content, or if the problem marker is missing or malformed
     */
    @Override
    public String content() throws InvariantViolation {
        return this.origin.content();
    }
}
