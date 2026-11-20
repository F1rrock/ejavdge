package org.ejavdge.domain.problem;

import org.ejavdge.contest.Attachment;
import org.ejavdge.contest.ContestResource;
import org.ejavdge.contest.MainPage;
import org.ejavdge.dom.engine.XmlEngine;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.scalar.text.Stencil;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.web.context.ProbId;

/**
 * The optional Java header of a contest problem.
 * <p>
 * The file is fetched as an ejudge {@link Attachment} named
 * {@code HeaderOf<problem>.java}, where {@code <problem>} is the problem
 * name. Its bytes go before the solution source during local testing.
 */
public final class ProbHeader implements Bytes {
    /**
     * The source of the header bytes.
     */
    private final Bytes origin;

    /**
     * Creates a problem header whose ID is resolved from the contest main page.
     *
     * @param n the problem name used for lookup and in the attachment file name
     * @param r the authenticated contest resource
     * @param e the XML engine used to find the problem ID
     */
    public ProbHeader(final Text n, final ContestResource r, final XmlEngine e) {
        this(
            n,
            new ProbByName(e, new MainPage(r), n),
            r
        );
    }

    /**
     * Creates a problem header for an already known problem ID.
     *
     * @param n the problem name used in the attachment file name
     * @param p the problem ID
     * @param r the authenticated contest resource
     */
    public ProbHeader(final Text n, final Num p, final ContestResource r) {
        this(
            new Attachment(
                new Stencil(
                    new Text.Of("HeaderOf%s.java"),
                    n
                ),
                new ProbId(p),
                r
            )
        );
    }

    /**
     * Wraps an existing source of header bytes.
     *
     * @param bs the source of the header bytes
     */
    public ProbHeader(final Bytes bs) {
        this.origin = bs;
    }

    /**
     * Returns the Java header bytes.
     *
     * @return the header bytes, or empty bytes if the contest response does not
     *         provide an attachment
     * @throws InvariantViolation if the underlying byte source fails
     */
    @Override
    public byte[] content() throws InvariantViolation {
        return this.origin.content();
    }
}
