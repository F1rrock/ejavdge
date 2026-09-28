package org.ejavdge.domain.run;

import junit.framework.TestCase;
import org.ejavdge.contest.ProblemPage;
import org.ejavdge.dom.engine.JsoupWithSaxon;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

public final class LastRunInfoTest extends TestCase {
    private static final String PAGE = """
        <html><body>
        <div id="ej-main-submit-tab">
            <table>
                <table class="table">
                    <tr>
                        <th class="b1">Run ID</th>
                        <th class="b1">Time</th>
                        <th class="b1">Size</th>
                    </tr>
                    <tr>
                        <td class="b1">9</td>
                        <td class="b1">484:35:05</td>
                        <td class="b1">514</td>
                    </tr>
                </table>
            </table>
        </div>
        </body></html>
        """;

    public void testFullInfo() {
        assertEquals(
            "Run ID: 9\nTime: 484:35:05\nSize: 514",
            new LastRunInfo(
                new JsoupWithSaxon(),
                new ProblemPage(new Text.Of(PAGE))
            ).content()
        );
    }

    public void testTwoColumns() {
        assertEquals(
            "Result: OK\nLanguage: r",
            new LastRunInfo(
                new JsoupWithSaxon(),
                new ProblemPage(new Text.Of("""
                    <html><body>
                    <div id="ej-main-submit-tab">
                        <table>
                            <table class="table">
                                <tr>
                                    <th class="b1">Result</th>
                                    <th class="b1">Language</th>
                                </tr>
                                <tr>
                                    <td class="b1">OK</td>
                                    <td class="b1">r</td>
                                </tr>
                            </table>
                        </table>
                    </div>
                    </body></html>
                    """))
            ).content()
        );
    }

    public void testHeaderLongerThanData() {
        assertEquals(
            "Run ID: 9\nTime: 484:35:05\nSize",
            new LastRunInfo(
                new JsoupWithSaxon(),
                new ProblemPage(new Text.Of("""
                    <html><body>
                    <div id="ej-main-submit-tab">
                        <table>
                            <table class="table">
                                <tr>
                                    <th class="b1">Run ID</th>
                                    <th class="b1">Time</th>
                                    <th class="b1">Size</th>
                                </tr>
                                <tr>
                                    <td class="b1">9</td>
                                    <td class="b1">484:35:05</td>
                                </tr>
                            </table>
                        </table>
                    </div>
                    </body></html>
                    """))
            ).content()
        );
    }

    public void testDataLongerThanHeader() {
        assertEquals(
            "Run ID: 9\nTime: 484:35:05\n514",
            new LastRunInfo(
                new JsoupWithSaxon(),
                new ProblemPage(new Text.Of("""
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
                                    <td class="b1">514</td>
                                </tr>
                            </table>
                        </table>
                    </div>
                    </body></html>
                    """))
            ).content()
        );
    }

    public void testWhitespaceTrimmed() {
        assertEquals(
            "Run ID: 9\nTime: 484:35:05",
            new LastRunInfo(
                new JsoupWithSaxon(),
                new ProblemPage(new Text.Of("""
                    <html><body>
                    <div id="ej-main-submit-tab">
                        <table>
                            <table class="table">
                                <tr>
                                    <th class="b1"> Run ID </th>
                                    <th class="b1">  Time  </th>
                                </tr>
                                <tr>
                                    <td class="b1"> 9 </td>
                                    <td class="b1"> 484:35:05 </td>
                                </tr>
                            </table>
                        </table>
                    </div>
                    </body></html>
                    """))
            ).content()
        );
    }

    public void testEmptyPage() {
        try {
            new LastRunInfo(
                new JsoupWithSaxon(),
                new ProblemPage(new Text.Of(""))
            ).content();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testWithoutTable() {
        try {
            new LastRunInfo(
                new JsoupWithSaxon(),
                new ProblemPage(new Text.Of(
                    """
                        <html><body>
                        <div id="ej-main-submit-tab"></div>
                        </body></html>
                        """
                ))
            ).content();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testWithHeadersOnly() {
        try {
            new LastRunInfo(
                new JsoupWithSaxon(),
                new ProblemPage(new Text.Of("""
                    <html><body>
                    <div id="ej-main-submit-tab">
                        <table>
                            <table class="table">
                                <tr>
                                    <th class="b1">Run ID</th>
                                    <th class="b1">Time</th>
                                </tr>
                            </table>
                        </table>
                    </div>
                    </body></html>
                    """))
            ).content();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testDataWithoutHeaders() {
        try {
            new LastRunInfo(
                new JsoupWithSaxon(),
                new ProblemPage(new Text.Of("""
                    <html><body>
                    <div id="ej-main-submit-tab">
                        <table>
                            <table class="table">
                                <tr>
                                    <td class="b1">9</td>
                                    <td class="b1">484:35:05</td>
                                </tr>
                            </table>
                        </table>
                    </div>
                    </body></html>
                    """))
            ).content();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testWrongDivId() {
        try {
            new LastRunInfo(
                new JsoupWithSaxon(),
                new ProblemPage(new Text.Of("""
                    <html><body>
                    <div id="other">
                        <table>
                            <table class="table">
                                <tr><th class="b1">Run ID</th></tr>
                                <tr><td class="b1">9</td></tr>
                            </table>
                        </table>
                    </div>
                    </body></html>
                    """))
            ).content();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testWrongTableClass() {
        try {
            new LastRunInfo(
                new JsoupWithSaxon(),
                new ProblemPage(new Text.Of("""
                    <html><body>
                    <div id="ej-main-submit-tab">
                        <table>
                            <table class="wrong">
                                <tr><th class="b1">Run ID</th></tr>
                                <tr><td class="b1">9</td></tr>
                            </table>
                        </table>
                    </div>
                    </body></html>
                    """))
            ).content();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testBrokenPage() {
        try {
            new LastRunInfo(
                new JsoupWithSaxon(),
                new ProblemPage(
                    () -> {
                        throw new InvariantViolation("There is no text.");
                    }
                )
            ).content();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testBrokenEngine() {
        try {
            new LastRunInfo(
                (xml, path) -> {
                    throw new InvariantViolation("There is no engine.");
                },
                new ProblemPage(new Text.Of(PAGE))
            ).content();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testFromText() {
        assertEquals(
            "custom info",
            new LastRunInfo(new Text.Of("custom info")).content()
        );
    }
}
