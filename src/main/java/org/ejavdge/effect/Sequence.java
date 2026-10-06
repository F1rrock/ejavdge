package org.ejavdge.effect;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.items.Items;

/**
 * An effect that performs a sequence of other effects in order.
 * <p>
 * This class implements {@link Effect} and represents a composite effect that
 * executes each of its constituent effects one after another, in the order they
 * are provided. If any effect in the sequence throws an
 * {@link InvariantViolation}, the sequence stops and the exception propagates
 * to the caller; subsequent effects are not performed.
 * <p>
 * The effects can be provided either as a varargs array or as an
 * {@link Items Items&lt;Effect&gt;} collection.
 */
public final class Sequence implements Effect {

    /**
     * The collection of effects to be performed in order.
     */
    private final Items<Effect> effects;

    /**
     * Creates a sequence from the given effects.
     *
     * @param es the effects to perform, in order
     */
    public Sequence(final Effect ...es) {
        this(new Items.Of<>(es));
    }

    /**
     * Creates a sequence from the given collection of effects.
     *
     * @param es the collection of effects to perform, in order
     */
    public Sequence(final Items<Effect> es) {
        this.effects = es;
    }

    /**
     * Performs this effect by executing each constituent effect in order.
     * <p>
     * The effects are performed sequentially. If any effect throws an
     * {@link InvariantViolation}, the sequence is interrupted and the exception
     * is propagated; remaining effects are not performed.
     *
     * @throws InvariantViolation if any effect in the sequence violates an
     *         invariant during execution
     */
    @Override
    public void perform() throws InvariantViolation {
        this.effects.contents().forEach(Effect::perform);
    }
}
