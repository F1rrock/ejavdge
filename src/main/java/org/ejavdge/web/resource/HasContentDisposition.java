package org.ejavdge.web.resource;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.items.Items;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.bytes.Memo;
import org.ejavdge.scalar.text.*;

/**
 * A {@link Bytes} decorator that verifies the disposition type of an HTTP
 * response.
 * <p>
 * The type extracted by {@link ContentDisposition} must equal the expected
 * type without regard to case. A matching response is returned unchanged,
 * including its headers and body.
 */
public final class HasContentDisposition implements Bytes {

    /**
     * The response whose disposition type is verified.
     */
    private final Bytes origin;

    /**
     * The expected disposition type.
     */
    private final Text expected;

    /**
     * The optional message used when the disposition type does not match.
     */
    private final Text message;

    /**
     * Creates a verifier with the default mismatch message.
     *
     * @param e the expected disposition type
     * @param bs the full HTTP response bytes
     */
    public HasContentDisposition(final Text e, final Bytes bs) {
        this(e, new Empty(), bs);
    }

    /**
     * Creates a verifier with an optional custom mismatch message.
     * An empty message uses the default description of the mismatch.
     *
     * @param e the expected disposition type
     * @param m the custom message used if the type does not match
     * @param bs the full HTTP response bytes
     */
    public HasContentDisposition(final Text e, final Text m, final Bytes bs) {
        this.origin = bs;
        this.expected = e;
        this.message = new NonEmpty(m);
    }

    /**
     * Returns the original response when its disposition type matches.
     * The response is read once per call and reused for comparison and return.
     *
     * @return the unmodified response bytes
     * @throws InvariantViolation if the type cannot be determined or does not
     *         equal the expected type, ignoring case
     */
    @Override
    public byte[] content() throws InvariantViolation {
        final var bs = new Memo(this.origin);
        final var e = new Lowers(this.expected).content();
        final var a = new Lowers(new ContentDisposition(bs)).content();
        if (e.equals(a)) {
            return bs.content();
        }
        throw new InvariantViolation(
            new Fallback(
                this.message,
                new Concat(
                    new Text.Of(" "),
                    new Items.Of<>(
                        new Text.Of("Expected that content disposition contains"),
                        new Text.Of(e),
                        new Text.Of("but got"),
                        new Text.Of(a)
                    )
                )
            ).content()
        );
    }
}
