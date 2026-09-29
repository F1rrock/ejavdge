package org.ejavdge.domain.report;

import junit.framework.TestCase;
import org.ejavdge.contest.StatusInJson;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

public final class ReportReadinessTest extends TestCase {
    public void testRealPollingRunning() throws InvariantViolation {
        assertFalse(readiness(
            "{ \"h\": 11, \"m\": 8, \"s\": 10, \"d\": 28, \"o\": 9, \"y\": 2026 }"
        ).ok());
    }

    public void testRealPollingFinished() throws InvariantViolation {
        assertTrue(readiness(
            "{ \"h\": 11, \"m\": 8, \"s\": 10, \"d\": 28, \"o\": 9, \"y\": 2026, \"z\": 1 }"
        ).ok());
    }

    public void testRealPollingFinishedWithFalse() throws InvariantViolation {
        assertFalse(readiness(
            "{ \"h\": 11, \"m\": 8, \"s\": 10, \"d\": 28, \"o\": 9, \"y\": 2026, \"z\": 0 }"
        ).ok());
    }

    public void testWithDifferentArgsOrder() throws InvariantViolation {
        assertTrue(readiness(
            "{ \"z\": 1, \"h\": 11, \"m\": 8, \"s\": 10, \"d\": 28, \"o\": 9, \"y\": 2026 }"
        ).ok());
    }

    public void testManySpacesAfterColon() throws InvariantViolation {
        assertTrue(readiness(
            "{ \"h\": 11, \"z\":                    1, \"y\": 2026 }"
        ).ok());
    }

    public void testWithoutSpacesAroundColon() throws InvariantViolation {
        assertTrue(readiness(
            "{\"h\":11,\"z\":1,\"y\":2026}"
        ).ok());
    }

    public void testBareTrue() throws InvariantViolation {
        assertTrue(readiness("z:1").ok());
    }

    public void testQuotedTrue() throws InvariantViolation {
        assertTrue(readiness("\"z\": \"1\"").ok());
    }

    public void testSingleQuoted() throws InvariantViolation {
        assertTrue(readiness("'z': '1'").ok());
    }

    public void testBacktickQuoted() throws InvariantViolation {
        assertTrue(readiness("`z`: `1`").ok());
    }

    public void testTrailingDigit() throws InvariantViolation {
        assertFalse(readiness("{\"z\":12}").ok());
    }

    public void testLeadingZero() throws InvariantViolation {
        assertFalse(readiness("{\"z\":01}").ok());
    }

    public void testDifferentKey() throws InvariantViolation {
        assertFalse(readiness("{\"status\":1}").ok());
    }

    public void testEmpty() throws InvariantViolation {
        assertFalse(readiness("").ok());
    }

    private static ReportReadiness readiness(final String s) {
        return new ReportReadiness(new StatusInJson(new Text.Of(s)));
    }
}
