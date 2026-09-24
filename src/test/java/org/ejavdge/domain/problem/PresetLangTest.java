package org.ejavdge.domain.problem;

import junit.framework.TestCase;
import org.ejavdge.contest.ProblemPage;
import org.ejavdge.dom.engine.JsoupWithSaxon;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

public final class PresetLangTest extends TestCase {
    public void testPresetLang() {
        assertEquals(
            82,
            new PresetLang(
                new JsoupWithSaxon(),
                new ProblemPage(new Text.Of(
                    """
                    <html><body>
                    <div id="ej-submit-tabs">
                        <div id="ej-main-submit-tab">
                            <form method="post">
                                <input type="hidden" name="prob_id" value="3">
                                <table class="b0">
                                    <input type="hidden" name="lang_id" value="82">
                                </table>
                            </form>
                        </div>
                    </div>
                    </body></html>
                    """
                ))
            ).value()
        );
    }

    public void testEmptyValue() {
        try {
            new PresetLang(
                new JsoupWithSaxon(),
                new ProblemPage(new Text.Of(
                    """
                    <html><body>
                    <div id="ej-submit-tabs">
                        <form>
                            <table class="b0">
                                <input name="lang_id" value="">
                            </table>
                        </form>
                    </div>
                    </body></html>
                    """
                ))
            ).value();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testNonNumericValue() {
        try {
            new PresetLang(
                new JsoupWithSaxon(),
                new ProblemPage(new Text.Of(
                    """
                    <html><body>
                    <div id="ej-submit-tabs">
                        <form>
                            <table class="b0">
                                <input name="lang_id" value="r">
                            </table>
                        </form>
                    </div>
                    </body></html>
                    """
                ))
            ).value();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testEmptyPage() {
        try {
            new PresetLang(
                new JsoupWithSaxon(),
                new ProblemPage(new Text.Of(""))
            ).value();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testBrokenPage() {
        try {
            new PresetLang(
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
            new PresetLang(
                (xml, path) -> {
                    throw new InvariantViolation("There is no engine.");
                },
                new ProblemPage(new Text.Of(
                    """
                    <html><body>
                    <div id="ej-submit-tabs">
                        <div id="ej-main-submit-tab">
                            <form method="post">
                                <input type="hidden" name="prob_id" value="3">
                                <table class="b0">
                                    <input type="hidden" name="lang_id" value="82">
                                </table>
                            </form>
                        </div>
                    </div>
                    </body></html>
                    """
                ))
            ).value();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }
}
