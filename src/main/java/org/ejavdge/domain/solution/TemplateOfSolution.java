package org.ejavdge.domain.solution;

import org.ejavdge.contest.ContestResource;
import org.ejavdge.domain.problem.ProbFooter;
import org.ejavdge.domain.problem.ProbHeader;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.bytes.Concat;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.scalar.text.Text;

/**
 * A Java source template that surrounds a solution with a problem header
 * and footer.
 * <p>
 * A contest template uses the optional {@link ProbHeader} and
 * {@link ProbFooter} attachments of the same problem. An absent fragment
 * contributes no bytes. Use {@link SourceFromTemplate} to apply the template
 * as a deferred source of bytes.
 */
public final class TemplateOfSolution {
    /**
     * The source of the bytes placed before the solution.
     */
    private final Bytes header;
    /**
     * The source of the bytes placed after the solution.
     */
    private final Bytes footer;

    /**
     * Creates a template from the given problem's contest attachments.
     *
     * @param i the problem ID
     * @param n the problem name used in the attachment file names
     * @param r the authenticated contest resource
     */
    public TemplateOfSolution(final int i, final String n, final ContestResource r) {
        this(new Num.Of(i), new Text.Of(n), r);
    }

    /**
     * Creates a template from the given problem's contest attachments.
     *
     * @param i the problem ID
     * @param n the problem name used in the attachment file names
     * @param r the authenticated contest resource
     */
    public TemplateOfSolution(final Num i, final Text n, final ContestResource r) {
        this(
            new ProbHeader(n, i, r),
            new ProbFooter(n, i, r)
        );
    }

    /**
     * Creates a template from existing sources of header and footer bytes.
     *
     * @param h the source of the header bytes
     * @param f the source of the footer bytes
     */
    public TemplateOfSolution(final Bytes h, final Bytes f) {
        this.header = h;
        this.footer = f;
    }

    /**
     * Joins the header, the solution source, and the footer in that order.
     * No separators or line breaks are inserted between the fragments.
     *
     * @param bs the original solution source
     * @return the complete Java source as bytes
     * @throws InvariantViolation if any part of the source cannot be obtained
     */
    byte[] applyTo(final Bytes bs) throws InvariantViolation {
        return new Concat(this.header, bs, this.footer).content();
    }
}
