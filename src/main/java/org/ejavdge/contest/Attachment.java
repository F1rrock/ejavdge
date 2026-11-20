package org.ejavdge.contest;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.BindOfBytes;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.bytes.BytesAbout;
import org.ejavdge.scalar.bytes.Fallback;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.scalar.text.TextOfNum;
import org.ejavdge.web.context.ProbId;
import org.ejavdge.web.context.WithEntry;
import org.ejavdge.web.resource.HasContentDisposition;
import org.ejavdge.web.resource.PayloadOf;

/**
 * A named attachment of a contest problem.
 * <p>
 * When requested, the attachment is fetched using ejudge action {@code 193}
 * with the problem ID and file name. A response whose {@code Content-Disposition}
 * type is {@code attachment} supplies the raw payload. If the fetched response
 * cannot be recognized as an attachment, an empty byte array is used. Failures
 * while fetching the response propagate to the caller.
 */
public final class Attachment implements Bytes {
    /**
     * The source of the attachment bytes.
     */
    private final Bytes origin;

    /**
     * Creates a deferred request for a problem attachment.
     *
     * @param n the attachment file name
     * @param p the problem ID
     * @param r the authenticated contest resource
     */
    public Attachment(final Text n, final ProbId p, final ContestResource r) {
        this(
            new BytesAbout(
                "ejudge attachment",
                new BindOfBytes(
                    new ContestResource(
                        r,
                        new WithEntry(
                            new Text.Of("action"),
                            new TextOfNum(193),
                            new WithEntry(
                                new Text.Of("file"),
                                n, p
                            )
                        )
                    ),
                    res ->  new Fallback(
                        new PayloadOf(
                            new HasContentDisposition(
                                new Text.Of("attachment"),
                                new Bytes.Of(res)
                            )
                        ),
                        new Bytes.Of(new byte[0])
                    )
                )
            )
        );
    }

    /**
     * Wraps an existing source of attachment bytes.
     *
     * @param bs the source of the attachment bytes
     */
    public Attachment(final Bytes bs) {
        this.origin = bs;
    }

    /**
     * Returns the attachment bytes.
     *
     * @return the file payload, or empty bytes if a contest response cannot be
     *         recognized as an attachment
     * @throws InvariantViolation if the response cannot be fetched or the
     *         supplied source fails
     */
    @Override
    public byte[] content() throws InvariantViolation {
        return this.origin.content();
    }
}
