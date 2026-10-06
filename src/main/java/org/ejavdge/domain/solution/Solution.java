package org.ejavdge.domain.solution;

import org.ejavdge.contest.ContestForm;
import org.ejavdge.effect.Envelope;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.file.ByteFile;
import org.ejavdge.items.Items;
import org.ejavdge.items.Joint;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.web.context.ContextOfSolution;
import org.ejavdge.web.context.WithEntry;
import org.ejavdge.web.spec.body.multipart.FilePart;
import org.ejavdge.web.spec.body.multipart.TextParts;

/**
 * A solution to be submitted to the ejudge contest system.
 * <p>
 * This class implements {@link Envelope} and represents a submission action
 * that sends a solution file to the contest system. It is constructed from a
 * {@link ContestForm} (which carries the driver, location, and session), a
 * {@link ContextOfSolution} (which specifies the problem and language), and a
 * {@link ByteFile} containing the solution source code.
 * <p>
 * The submission is performed as a multipart form POST. The form includes:
 * <ul>
 *   <li>a text part with the action {@code "action_40"} and value
 *       {@code "Send!"}, along with the solution context (problem and
 *       language);</li>
 *   <li>a file part containing the solution source code.</li>
 * </ul>
 * <p>
 * When {@link #send()} is called, the underlying contest form is submitted.
 */
public final class Solution implements Envelope {

    /**
     * The underlying envelope that performs the actual submission.
     */
    private final Envelope origin;

    /**
     * Creates a solution submission from the given contest form, solution
     * context, and solution file.
     * <p>
     * The contest form is extended with two parts:
     * <ol>
     *   <li>a text part containing the action field {@code "action_40"} with
     *       value {@code "Send!"} and the solution context (problem and
     *       language);</li>
     *   <li>a file part containing the solution source code.</li>
     * </ol>
     * The resulting form is used as the underlying envelope.
     *
     * @param cf the contest form carrying the driver, location, and session
     * @param cs the solution context specifying the problem and language
     * @param f  the solution file to submit
     */
    public Solution(final ContestForm cf, final ContextOfSolution cs, final ByteFile f) {
        this(
            new ContestForm(
                cf,
                new Joint<>(
                    new TextParts.ImprintOf(
                        new WithEntry(
                            new Text.Of("action_40"),
                            new Text.Of("Send!"),
                            cs
                        )
                    ),
                    new Items.Of<>(new FilePart(f))
                )
            )
        );
    }

    /**
     * Creates a solution submission by wrapping an existing envelope.
     *
     * @param e the envelope to wrap
     */
    public Solution(final Envelope e) {
        this.origin = e;
    }

    /**
     * Submits the solution to the contest system.
     * <p>
     * This method delegates to the underlying envelope's
     * {@link Envelope#send()} method, which performs the multipart POST request.
     *
     * @throws InvariantViolation if an invariant is violated during submission
     */
    @Override
    public void send() throws InvariantViolation {
        this.origin.send();
    }
}
