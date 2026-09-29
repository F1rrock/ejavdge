package org.ejavdge.domain.report;

import org.ejavdge.domain.Verdict;
import org.ejavdge.effect.Effect;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

public final class AffirmingOf implements Effect {
    private final Verdict src;
    private final Text message;

    public AffirmingOf(final Verdict v, final Text m) {
        this.src = v;
        this.message = m;
    }

    @Override
    public void perform() throws InvariantViolation {
        if (!this.src.ok()) {
            throw new InvariantViolation(this.message.content());
        }
    }
}
