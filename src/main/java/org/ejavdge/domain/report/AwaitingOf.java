package org.ejavdge.domain.report;

import org.ejavdge.domain.Verdict;
import org.ejavdge.effect.Effect;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.effect.WithDelay;

import java.time.Duration;

public final class AwaitingOf implements Effect {
    private final Verdict verdict;
    private final Duration duration;

    public AwaitingOf(final Verdict v, final Duration d) {
        this.verdict = v;
        this.duration = d;
    }

    @Override
    public void perform() throws InvariantViolation {
        if (!this.verdict.ok()) {
            new WithDelay(this, this.duration).perform();
        }
    }
}
