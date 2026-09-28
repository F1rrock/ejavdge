package org.ejavdge.domain.run;

import junit.framework.TestCase;
import org.ejavdge.contest.ProblemPage;
import org.ejavdge.dom.engine.JsoupWithSaxon;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

public final class LastRunIdTest extends TestCase {
    private static final String PAGE = """
        <html><body>
        <div id="ej-main-submit-tab">
            <table>
                <table class="table">
                    <tr>
                        <th class="b1">Run ID</th>
                        <th class="b1">Time</th>
                    </tr>
                    <tr>
                        <td class="b1">9</td>
                        <td class="b1">484:35:05</td>
                    </tr>
                </table>
            </table>
        </div>
        </body></html>
        """;

    public void testLastRunId() {
        assertEquals(
            9,
            new LastRunId(
                new JsoupWithSaxon(),
                new ProblemPage(new Text.Of(PAGE))
            ).value()
        );
    }

    public void testAnotherId() {
        assertEquals(
            42,
            new LastRunId(
                new JsoupWithSaxon(),
                new ProblemPage(new Text.Of("""
                    <html><body>
                    <div id="ej-main-submit-tab">
                        <table>
                            <table class="table">
                                <tr>
                                    <th class="b1">Run ID</th>
                                </tr>
                                <tr>
                                    <td class="b1">42</td>
                                </tr>
                            </table>
                        </table>
                    </div>
                    </body></html>
                    """))
            ).value()
        );
    }

    public void testIdWithWhitespace() {
        assertEquals(
            7,
            new LastRunId(
                new JsoupWithSaxon(),
                new ProblemPage(new Text.Of("""
                    <html><body>
                    <div id="ej-main-submit-tab">
                        <table>
                            <table class="table">
                                <tr>
                                    <th class="b1">Run ID</th>
                                </tr>
                                <tr>
                                    <td class="b1">
                                        7
                                    </td>
                                </tr>
                            </table>
                        </table>
                    </div>
                    </body></html>
                    """))
            ).value()
        );
    }

    public void testEmptyPage() {
        try {
            new LastRunId(
                new JsoupWithSaxon(),
                new ProblemPage(new Text.Of(""))
            ).value();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testWithoutTable() {
        try {
            new LastRunId(
                new JsoupWithSaxon(),
                new ProblemPage(new Text.Of(
                    """
                    <html><body>
                    <div id="ej-main-submit-tab"></div>
                    </body></html>
                    """
                ))
            ).value();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testWithoutSecondRow() {
        try {
            new LastRunId(
                new JsoupWithSaxon(),
                new ProblemPage(new Text.Of("""
                    <html><body>
                    <div id="ej-main-submit-tab">
                        <table>
                            <table class="table">
                                <tr>
                                    <th class="b1">Run ID</th>
                                </tr>
                            </table>
                        </table>
                    </div>
                    </body></html>
                    """))
            ).value();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testEmptyCell() {
        try {
            new LastRunId(
                new JsoupWithSaxon(),
                new ProblemPage(new Text.Of("""
                    <html><body>
                    <div id="ej-main-submit-tab">
                        <table>
                            <table class="table">
                                <tr>
                                    <th class="b1">Run ID</th>
                                </tr>
                                <tr>
                                    <td class="b1"></td>
                                </tr>
                            </table>
                        </table>
                    </div>
                    </body></html>
                    """))
            ).value();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testNonNumericId() {
        try {
            new LastRunId(
                new JsoupWithSaxon(),
                new ProblemPage(new Text.Of("""
                    <html><body>
                    <div id="ej-main-submit-tab">
                        <table>
                            <table class="table">
                                <tr>
                                    <th class="b1">Run ID</th>
                                </tr>
                                <tr>
                                    <td class="b1">abc</td>
                                </tr>
                            </table>
                        </table>
                    </div>
                    </body></html>
                    """))
            ).value();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testBrokenPage() {
        try {
            new LastRunId(
                new JsoupWithSaxon(),
                new ProblemPage(
                    () -> {
                        throw new InvariantViolation("There is no text.");
                    }
                )
            ).value();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testBrokenEngine() {
        try {
            new LastRunId(
                (xml, path) -> {
                    throw new InvariantViolation("There is no engine.");
                },
                new ProblemPage(new Text.Of(PAGE))
            ).value();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }
}
