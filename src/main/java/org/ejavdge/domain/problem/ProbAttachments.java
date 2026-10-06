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
 * A collection of file references (attachments) associated with a problem.
 * <p>
 * This class implements {@link Items Items&lt;Text&gt;} and represents the
 * attachment links found on a problem page. The links are extracted from anchor
 * ({@code <a>}) elements that are nested inside elements carrying the
 * {@code name="localtest"} attribute. The extracted {@code href} values are
 * split into lines, trimmed, and filtered to remove empty entries. The
 * resulting collection is labelled as "problem's file references".
 * <p>
 * An instance can be created either by extracting the attachments from a
 * problem page using an XML engine, or by wrapping an existing collection of
 * text items.
 */
public final class ProbAttachments implements Items<Text> {

    /**
     * The underlying collection of attachment references.
     */
    private final Items<Text> origin;

    /**
     * Creates a collection of problem attachments by extracting them from the
     * given problem page.
     * <p>
     * The extraction process is as follows:
     * <ol>
     *   <li>Select all elements with {@code name="localtest"} on the problem
     *       page.</li>
     *   <li>Within those elements, find all anchor ({@code <a>}) tags and
     *       extract their {@code href} attributes.</li>
     *   <li>Join the extracted links into a single string, separated by
     *       newlines.</li>
     *   <li>Split the string into individual lines, trim each line, and discard
     *       empty lines.</li>
     * </ol>
     * The resulting collection is wrapped with a descriptive label
     * "problem's file references".
     *
     * @param e the XML engine used to evaluate the XPath selection
     * @param p the problem page from which attachments are extracted
     */
    public ProbAttachments(final XmlEngine e, final ProblemPage p) {
        this(
            new ItemsAbout<>(
                "problem's file references",
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
     * Creates a collection of problem attachments by wrapping an existing
     * collection of text items.
     *
     * @param ts the collection of text items to wrap
     */
    public ProbAttachments(final Items<Text> ts) {
        this.origin = ts;
    }

    /**
     * Returns the list of attachment references.
     *
     * @return the list of text items representing attachment references
     * @throws InvariantViolation if an invariant is violated while retrieving
     *         the contents
     */
    @Override
    public List<Text> contents() throws InvariantViolation {
        return this.origin.contents();
    }
}
