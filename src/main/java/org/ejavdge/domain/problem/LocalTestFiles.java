package org.ejavdge.domain.problem;

import org.ejavdge.contest.ProblemPage;
import org.ejavdge.dom.XmlSelection;
import org.ejavdge.dom.engine.XmlEngine;
import org.ejavdge.dom.path.AllNodes;
import org.ejavdge.dom.path.LinksOnly;
import org.ejavdge.dom.path.WithName;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.items.*;
import org.ejavdge.scalar.text.ContentBased;
import org.ejavdge.scalar.text.Empty;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.scalar.text.Trimmed;

import java.util.List;

/**
 * Links to the files a solution reads from its working directory during a
 * local run. By the university convention, these links appear in the problem
 * statement with the {@code name="localtest"} attribute and can point to any host.
 */
public final class LocalTestFiles implements Items<Text> {

    /**
     * The underlying collection of Local Test file links.
     */
    private final Items<Text> origin;

    /**
     * Extracts Local Test file links from the given problem page.
     * <p>
     * The extraction process is as follows:
     * <ol>
     *   <li>Extract the {@code href} values from links marked
     *       {@code name="localtest"}.</li>
     *   <li>Join the extracted links into a single string, separated by
     *       newlines.</li>
     *   <li>Split the string into individual lines, trim each line, and discard
     *       empty lines.</li>
     * </ol>
     *
     * @param e the XML engine used to evaluate the XPath selection
     * @param p the problem page from which links are extracted
     */
    public LocalTestFiles(final XmlEngine e, final ProblemPage p) {
        this(
            new ItemsAbout<>(
                "Local Test file links",
                new OnlyWhere<>(
                    l -> !new ContentBased(l).equals(new ContentBased(new Empty())),
                    new Map<>(
                        Trimmed::new,
                        new Lines(
                            new XmlSelection(
                                e, p,
                                new LinksOnly(
                                    new WithName(
                                        "localtest",
                                        new AllNodes()
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
     * Wraps an existing collection of Local Test file links.
     *
     * @param ts the links to wrap
     */
    public LocalTestFiles(final Items<Text> ts) {
        this.origin = ts;
    }

    /**
     * Returns the Local Test file links.
     *
     * @return the list of Local Test file links
     * @throws InvariantViolation if an invariant is violated while retrieving
     *         the contents
     */
    @Override
    public List<Text> contents() throws InvariantViolation {
        return this.origin.contents();
    }
}
