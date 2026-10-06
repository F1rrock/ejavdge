package org.ejavdge.domain.report;

import org.ejavdge.contest.StatusInJson;
import org.ejavdge.domain.Verdict;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Match;
import org.ejavdge.scalar.text.Text;

/**
 * A verdict that determines whether a run report is ready for retrieval.
 * <p>
 * In the ejudge contest system, a run may not have a complete report
 * immediately after submission. The JSON status of a run contains a field
 * (typically {@code "z"}) that indicates readiness. This class inspects that
 * status and reports whether the report is available.
 * <p>
 * Readiness is determined by checking the JSON status string for the presence
 * of a field {@code "z"} with the value {@code 1}. The check is performed with
 * a regular expression that tolerates optional surrounding quotes and
 * whitespace, for example {@code "z":1}, {@code 'z': 1}, or {@code z:1}. If
 * such a pattern is found, the verdict is considered successful.
 * <p>
 * If the status content cannot be retrieved, the exception propagates. If the
 * pattern is simply not found, the verdict returns {@code false} rather than
 * throwing.
 */
public final class ReportReadiness implements Verdict {

    /**
     * The JSON status of the run whose report readiness is being checked.
     */
    private final StatusInJson status;

    /**
     * Creates a new readiness verdict backed by the given JSON status.
     *
     * @param s the JSON status to inspect
     */
    public ReportReadiness(final StatusInJson s) {
        this.status = s;
    }

    /**
     * Returns whether the report is ready.
     * <p>
     * The method retrieves the JSON status content and attempts to match a
     * pattern corresponding to a {@code "z"} field with value {@code 1}. If the
     * pattern is found, the method returns {@code true}. If the pattern is not
     * found (i.e., {@link Match} throws an {@link InvariantViolation}), the
     * method catches the exception and returns {@code false}.
     * <p>
     * If retrieving the status content itself throws an
     * {@link InvariantViolation}, that exception is not caught and propagates
     * to the caller.
     *
     * @return {@code true} if the report is ready, {@code false} otherwise
     * @throws InvariantViolation if the status content cannot be retrieved
     */
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
