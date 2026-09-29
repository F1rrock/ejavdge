package org.ejavdge.domain.report;

import org.ejavdge.domain.Verdict;
import org.ejavdge.effect.Effect;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.effect.WithDelay;

import java.time.Duration;

public final class WithPolling implements Effect {
    private final Effect origin;
    private final Verdict verdict;
    private final Duration duration;

    public WithPolling(final Effect e, final Verdict v, final Duration d) {
        this.origin = e;
        this.verdict = v;
        this.duration = d;
    }

    @Override
    public void perform() throws InvariantViolation {
        if (this.verdict.ok()) {
            this.origin.perform();
        } else {
            new WithDelay(this, this.duration).perform();
        }
    }
}
