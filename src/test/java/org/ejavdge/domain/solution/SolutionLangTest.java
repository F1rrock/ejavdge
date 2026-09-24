package org.ejavdge.domain.solution;

import junit.framework.TestCase;
import org.ejavdge.contest.ProblemPage;
import org.ejavdge.dom.engine.JsoupWithSaxon;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.file.ByteFile;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.text.Text;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;

public final class SolutionLangTest extends TestCase {
    private static final String PAGE_WITH_BOTH = """
        <html><body>
        <div id="ej-submit-tabs">
            <form>
                <table class="b0">
                    <input type="hidden" name="lang_id" value="82">
                </table>
            </form>
        </div>
        <form>
            <select name="lang_id">
                <option value="1">Choose language</option>
                <option value="82">r - R 4.4.3</option>
                <option value="83">python - Python 3</option>
            </select>
        </form>
        </body></html>
        """;

    private static final String PAGE_WITHOUT_PRESET = """
        <html><body>
        <form>
            <select name="lang_id">
                <option value="1">Choose language</option>
                <option value="82">r - R 4.4.3</option>
                <option value="83">python - Python 3</option>
            </select>
        </form>
        </body></html>
        """;

    private static final String PAGE_EMPTY = """
        <html><body></body></html>
        """;

    public void testWithPresetLang() {
        assertEquals(
            82,
            new SolutionLang(
                new JsoupWithSaxon(),
                new ProblemPage(new Text.Of(PAGE_WITH_BOTH)),
                this.file("// language: 2\nint main() {}")
            ).value()
        );
    }

    public void testWithoutPresetLang() {
        assertEquals(
            83,
            new SolutionLang(
                new JsoupWithSaxon(),
                new ProblemPage(new Text.Of(PAGE_WITHOUT_PRESET)),
                this.file("// language: 2\nint main() {}")
            ).value()
        );
    }

    public void testPresetWithoutMarker() {
        assertEquals(
            82,
            new SolutionLang(
                new JsoupWithSaxon(),
                new ProblemPage(new Text.Of(PAGE_WITH_BOTH)),
                this.file("int main() {}")
            ).value()
        );
    }

    public void testWithoutPresetAndMarker() {
        try {
            new SolutionLang(
                new JsoupWithSaxon(),
                new ProblemPage(new Text.Of(PAGE_WITHOUT_PRESET)),
                this.file("int main() {}")
            ).value();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testLangMarkerOutOfBounds() {
        try {
            new SolutionLang(
                new JsoupWithSaxon(),
                new ProblemPage(new Text.Of(PAGE_WITHOUT_PRESET)),
                this.file("// language: 10\nint main() {}")
            ).value();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testEmptyPage() {
        try {
            new SolutionLang(
                new JsoupWithSaxon(),
                new ProblemPage(new Text.Of(PAGE_EMPTY)),
                this.file("int main() {}")
            ).value();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testBrokenPage() {
        try {
            new SolutionLang(
                new JsoupWithSaxon(),
                new ProblemPage(
                    () -> {
                        throw new InvariantViolation("There is no text.");
                    }
                ),
                this.file("// language: 2\nint main() {}")
            ).value();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testBrokenEngine() {
        try {
            new SolutionLang(
                (xml, path) -> {
                    throw new InvariantViolation("There is no engine.");
                },
                new ProblemPage(new Text.Of(PAGE_WITH_BOTH)),
                this.file("// language: 2\nint main() {}")
            ).value();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testBrokenFile() {
        try {
            new SolutionLang(
                new JsoupWithSaxon(),
                new ProblemPage(new Text.Of(PAGE_WITHOUT_PRESET)),
                new ByteFile.Of(
                    new Text.Of("broken"),
                    () -> {
                        throw new InvariantViolation("There is no contents.");
                    }
                )
            ).value();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testOfFileCallsWithPreset() throws InvariantViolation {
        final var calls = new AtomicInteger(0);
        new SolutionLang(
            new JsoupWithSaxon(),
            new ProblemPage(new Text.Of(PAGE_WITH_BOTH)),
            new ByteFile.Of(
                new Text.Of("broken"),
                () -> {
                    calls.incrementAndGet();
                    return "// language: 2\nint main() {}".getBytes(
                        StandardCharsets.UTF_8
                    );
                }
            )
        ).value();
        assertEquals(0, calls.get());
    }

    public void testOfFileCallsWithoutPreset() throws InvariantViolation {
        final var calls = new AtomicInteger(0);
        new SolutionLang(
            new JsoupWithSaxon(),
            new ProblemPage(new Text.Of(PAGE_WITHOUT_PRESET)),
            new ByteFile.Of(
                new Text.Of("broken"),
                () -> {
                    calls.incrementAndGet();
                    return "// language: 2\nint main() {}".getBytes(
                        StandardCharsets.UTF_8
                    );
                }
            )
        ).value();
        assertEquals(1, calls.get());
    }

    public void testOfProblemCallsWithPreset() {
        final var calls = new AtomicInteger(0);
        new SolutionLang(
            new JsoupWithSaxon(),
            new ProblemPage(
                () -> {
                    calls.incrementAndGet();
                    return PAGE_WITH_BOTH;
                }
            ),
            this.file("// language: 2\nint main() {}")
        ).value();
        assertEquals(1, calls.get());
    }

    public void testOfProblemCallsWithoutPreset() {
        final var calls = new AtomicInteger(0);
        new SolutionLang(
            new JsoupWithSaxon(),
            new ProblemPage(
                () -> {
                    calls.incrementAndGet();
                    return PAGE_WITHOUT_PRESET;
                }
            ),
            this.file("// language: 2\nint main() {}")
        ).value();
        assertEquals(1, calls.get());
    }

    private ByteFile file(final String content) {
        return new ByteFile.Of(
            new Text.Of("Main.java"),
            new Bytes.Of(content.getBytes(StandardCharsets.UTF_8))
        );
    }
}
