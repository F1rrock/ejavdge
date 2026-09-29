package org.ejavdge.domain.report;

import org.ejavdge.contest.StatusInJson;
import org.ejavdge.domain.Verdict;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Match;
import org.ejavdge.scalar.text.Text;

public final class ReportReadiness implements Verdict {
    private final StatusInJson status;

    public ReportReadiness(final StatusInJson s) {
        this.status = s;
    }

    @Override
    public boolean ok() throws InvariantViolation {
        final var s = this.status.content();
        try {
            new Match(
                new Text.Of(s),
                new Text.Of(
                    "[\"'`]?z[\"'`]?\\s*:\\s*[\"'`]?1[\"'`]?(?!\\d)"
                )
            ).content();
            return true;
        } catch (final InvariantViolation ignored) {
            return false;
        }
    }
}
