package org.ejavdge.domain.run;

import org.ejavdge.contest.ProblemPage;
import org.ejavdge.dom.XmlSelection;
import org.ejavdge.dom.engine.XmlEngine;
import org.ejavdge.dom.path.*;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.items.*;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.scalar.text.*;

/**
 * A textual summary of the most recent run of a problem, extracted from a
 * problem page.
 * <p>
 * This class implements {@link Text} and produces a human-readable description
 * of the last run shown in the problem's run table. The table typically has a
 * header row listing column labels (such as date, verdict, compiler, etc.) and
 * a data row with the corresponding values for the last run. This class pairs
 * those two rows line by line, prefixing each value with {@code ": "} to form
 * entries like {@code ": OK"} or {@code ": 2024-01-01"}.
 * <p>
 * The extraction is performed using an {@link XmlEngine} and a chain of
 * {@link org.ejavdge.dom.path.DocPath} selections:
 * <ol>
 *   <li>Locate the table rows on the problem page (via
 *       {@code PathToTableRows}).</li>
 *   <li>Extract the inner text of the first row (column labels) and split it
 *       into significant lines.</li>
 *   <li>Extract the inner text of the second row (values for the last run) and
 *       split it into significant lines. If this row is empty, the fallback
 *       message {@code "There is no reports yet."} is used.</li>
 *   <li>Zip the two lists of lines together and prefix each value with
 *       {@code ": "}.</li>
 * </ol>
 * The resulting lines are joined with newlines, and the final text is labelled
 * as "problem's last run info" and prefixed with an additional newline.
 * <p>
 * An instance can also be created by wrapping an existing {@link Text}.
 */
public final class LastRunInfo implements Text {

    /**
     * The underlying textual content of the last run information.
     */
    private final Text origin;

    /**
     * Creates a last run info text by extracting it from the given problem page
     * using the provided XML engine.
     * <p>
     * The extraction follows these steps:
     * <ol>
     *   <li>Select the table rows on the problem page.</li>
     *   <li>Take the inner text of the first row, split it into significant
     *       lines — these represent the column labels.</li>
     *   <li>Take the inner text of the second row, split it into significant
     *       lines — these represent the values of the last run. If this text is
     *       empty, the fallback message {@code "There is no reports yet."} is
     *       used.</li>
     *   <li>Zip the labels and values together, prefixing each value with
     *       {@code ": "}.</li>
     * </ol>
     * The final text is labelled as "problem's last run info" and prefixed with
     * a newline.
     *
     * @param e the XML engine used to evaluate the XPath selections
     * @param p the problem page from which the last run information is
     *          extracted
     */
    public LastRunInfo(final XmlEngine e, final ProblemPage p) {
        this(
            new TextAbout(
                "problem's last run info",
                new BindOfText(
                    p,
                    page -> new Concat(
                        new Text.Of("\n"),
                        new Map<>(
                            row -> new Concat(new Text.Of(": "), row),
                            new ZipOf<>(
                                new SignificantLines(
                                    new XmlSelection(
                                        e,
                                        new ProblemPage(new Text.Of(page)),
                                        new InnerText(
                                            new Text.Of("\n"),
                                            new OnlyAt(
                                                new Num.Of(1),
                                                new PathToTableRows()
                                            )
                                        )
                                    )
                                ),
                                new SignificantLines(
                                    new NonEmpty(
                                        new XmlSelection(
                                            e,
                                            new ProblemPage(new Text.Of(page)),
                                            new InnerText(
                                                new Text.Of("\n"),
                                                new OnlyAt(
                                                    new Num.Of(2),
                                                    new PathToTableRows()
                                                )
                                            )
                                        ),
                                        new Text.Of("There is no reports yet.")
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
     * Creates a last run info text by wrapping an existing text.
     *
     * @param t the text that will become the last run info content
     */
    public LastRunInfo(final Text t) {
        this.origin = t;
    }

    /**
     * Returns the textual content of the last run information.
     *
     * @return the last run info as a newline-separated string
     * @throws InvariantViolation if an invariant is violated while retrieving
     *         the content
     */
    @Override
    public String content() throws InvariantViolation {
        return this.origin.content();
    }
}
