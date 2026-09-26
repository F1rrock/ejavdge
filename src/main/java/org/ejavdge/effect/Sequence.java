package org.ejavdge.effect;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.items.Items;

public final class Sequence implements Effect {
    private final Items<Effect> effects;

    public Sequence(final Effect ...es) {
        this(new Items.Of<>(es));
    }

    public Sequence(final Items<Effect> es) {
        this.effects = es;
    }


    @Override
    public void perform() throws InvariantViolation {
        this.effects.contents().forEach(Effect::perform);
    }
}
