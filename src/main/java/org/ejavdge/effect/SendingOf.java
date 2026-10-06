package org.ejavdge.effect;

import org.ejavdge.error.InvariantViolation;

/**
 * An effect that sends an {@link Envelope}.
 * <p>
 * This class adapts an {@link Envelope} to the {@link Effect} interface by
 * delegating the {@link Effect#perform()} call to the envelope's
 * {@link Envelope#send()} method. It allows envelopes — which represent
 * sendable requests or payloads — to be used wherever an effect is expected,
 * such as in effect pipelines or application entry points.
 */
public final class SendingOf implements Effect {

    /**
     * The envelope to be sent by this effect.
     */
    private final Envelope src;

    /**
     * Creates a new effect that will send the given envelope.
     *
     * @param e the envelope to send
     */
    public SendingOf(final Envelope e) {
        this.src = e;
    }

    /**
     * Performs this effect by sending the underlying envelope.
     *
     * @throws InvariantViolation if an invariant is violated during sending
     */
    @Override
    public void perform() throws InvariantViolation {
        this.src.send();
    }
}
