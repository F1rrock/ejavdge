package org.ejavdge.domain.report;

import org.ejavdge.domain.Verdict;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

public final class Feedback implements Text {
    private final Text onSuccess;
    private final Text onFail;
    private final Verdict verdict;

    public Feedback(final Text s, final Text f, final Verdict v) {
        this.onSuccess = s;
        this.onFail = f;
        this.verdict = v;
    }

    @Override
    public String content() throws InvariantViolation {
        if (this.verdict.ok()) {
            return this.onSuccess.content();
        } else {
            return this.onFail.content();
        }
    }
}
