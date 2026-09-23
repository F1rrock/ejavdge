package org.ejavdge.domain.problem;

import junit.framework.TestCase;
import org.ejavdge.contest.ProblemPage;
import org.ejavdge.dom.engine.JsoupWithSaxon;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.scalar.text.Text;

public final class LangByIndexTest extends TestCase {
    private static final String PAGE = """
        <html><body>
        <form>
            <select name="lang_id">
                <option value="1">Choose language</option>
                <option value="82">r - R 4.4.3</option>
                <option value="83">python - Python 3</option>
                <option value="84">java - Java 17</option>
            </select>
        </form>
        </body></html>
        """;

    public void testFirstLanguage() {
        assertEquals(
            82,
            new LangByIndex(
                new JsoupWithSaxon(),
                new ProblemPage(new Text.Of(PAGE)),
                new Num.Of(1)
            ).value()
        );
    }

    public void testSecondLanguage() {
        assertEquals(
            83,
            new LangByIndex(
                new JsoupWithSaxon(),
                new ProblemPage(new Text.Of(PAGE)),
                new Num.Of(2)
            ).value()
        );
    }

    public void testThirdLanguage() {
        assertEquals(
            84,
            new LangByIndex(
                new JsoupWithSaxon(),
                new ProblemPage(new Text.Of(PAGE)),
                new Num.Of(3)
            ).value()
        );
    }

    public void testOutOfBounds() {
        try {
            new LangByIndex(
                new JsoupWithSaxon(),
                new ProblemPage(new Text.Of(PAGE)),
                new Num.Of(10)
            ).value();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testZeroIndex() {
        try {
            new LangByIndex(
                new JsoupWithSaxon(),
                new ProblemPage(new Text.Of(PAGE)),
                new Num.Of(0)
            ).value();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testNegativeIndex() {
        try {
            new LangByIndex(
                new JsoupWithSaxon(),
                new ProblemPage(new Text.Of(PAGE)),
                new Num.Of(-1)
            ).value();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testWithoutSelect() {
        try {
            new LangByIndex(
                new JsoupWithSaxon(),
                new ProblemPage(new Text.Of(
                    """
                    <html><body>
                    <form><input type="hidden" name="lang_id" value="82"></form>
                    </body></html>
                    """
                )),
                new Num.Of(1)
            ).value();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testEmptyPage() {
        try {
            new LangByIndex(
                new JsoupWithSaxon(),
                new ProblemPage(new Text.Of("")),
                new Num.Of(1)
            ).value();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testBrokenPage() {
        try {
            new LangByIndex(
                new JsoupWithSaxon(),
                new ProblemPage(
                    () -> {
                        throw new InvariantViolation("There is no text.");
                    }
                ),
                new Num.Of(1)
            ).value();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testBrokenEngine() {
        try {
            new LangByIndex(
                (xml, path) -> {
                    throw new InvariantViolation("There is no engine.");
                },
                new ProblemPage(new Text.Of(PAGE)),
                new Num.Of(1)
            ).value();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testBrokenIndex() {
        try {
            new LangByIndex(
                new JsoupWithSaxon(),
                new ProblemPage(new Text.Of(PAGE)),
                () -> {
                    throw new InvariantViolation("There is no index.");
                }
            ).value();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }
}
