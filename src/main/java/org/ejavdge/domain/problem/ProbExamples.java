package org.ejavdge.domain.problem;

import org.ejavdge.domain.Fixture;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.items.*;
import org.ejavdge.scalar.text.*;

import java.util.List;

/**
 * A collection of example fixtures (input/output pairs) for a problem.
 * <p>
 * This class implements {@link Items Items&lt;Fixture&gt;} and represents the
 * test examples found in a problem's brief. Each example consists of an input
 * data block and a corresponding output data block, as shown in the problem
 * statement.
 * <p>
 * The examples are parsed from the textual brief of a problem. The parsing
 * process is as follows:
 * <ol>
 *   <li>Split the brief into lines and take only the portion after the line
 *       equal to "Examples".</li>
 *   <li>Split that portion into groups separated by lines equal to
 *       "Input", discarding the first (empty) group.</li>
 *   <li>For each group, further split it at the line equal to "Output" into
 *       two parts:
 *       <ul>
 *         <li>the input data — everything before "Output";</li>
 *         <li>the output data — everything after "Output".</li>
 *       </ul>
 *   </li>
 *   <li>Wrap each part in a {@link Fixture.Of}, trimming the result and
 *       labelling it accordingly.</li>
 * </ol>
 * <p>
 * An instance can also be created by wrapping an existing collection of
 * fixtures.
 */
public final class ProbExamples implements Items<Fixture> {

    /**
     * The underlying collection of example fixtures.
     */
    private final Items<Fixture> origin;

    /**
     * Creates a collection of problem examples by parsing them from the given
     * problem brief.
     * <p>
     * The parsing logic extracts the input and output data blocks for each
     * example as described in the class documentation. Input blocks are
     * labelled as "input data of problem example" and output blocks as
     * "output data of problem example". Both are trimmed and prefixed with a
     * newline for readability.
     *
     * @param b the problem brief from which examples are extracted
     */
    public ProbExamples(final ProbBrief b) {
        this(
            new Map<>(
                ls -> new Fixture.Of(
                    new TextAbout(
                        "input data of problem example",
                        new Trimmed(
                            new Concat(
                                new Text.Of("\n"),
                                new OnlyUntil<>(
                                    l -> l.equals(new ContentBased("Output")),
                                    ls
                                )
                            )
                        )
                    ),
                    new TextAbout(
                        "output data of problem example",
                        new Trimmed(
                            new Concat(
                                new Text.Of("\n"),
                                new OnlyAfter<>(
                                    l -> l.equals(new ContentBased("Output")),
                                    ls
                                )
                            )
                        )
                    )
                ),
                new Map<>(
                    ls -> new BindOfItems<>(ls, Items.Of::new),
                    new WithoutFirst<>(
                        new Split<>(
                            l -> l.equals(new ContentBased("Input")),
                            new OnlyAfter<>(
                                l -> l.equals(new ContentBased("Examples")),
                                new Map<>(
                                    ContentBased::new,
                                    new Lines(b)
                                )
                            )
                        )
                    )
                )
            )
        );
    }

    /**
     * Creates a collection of problem examples by wrapping an existing
     * collection of fixtures.
     *
     * @param xs the collection of fixtures to wrap
     */
    public ProbExamples(final Items<Fixture> xs) {
        this.origin = xs;
    }

    /**
     * Returns the list of example fixtures.
     *
     * @return the list of example fixtures
     * @throws InvariantViolation if an invariant is violated while retrieving
     *         the contents
     */
    @Override
    public List<Fixture> contents() throws InvariantViolation {
        return this.origin.contents();
    }
}
