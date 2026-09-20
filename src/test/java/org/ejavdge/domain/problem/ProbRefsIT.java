package org.ejavdge.domain.problem;

import junit.framework.TestCase;
import org.ejavdge.contest.ProblemPage;
import org.ejavdge.dom.engine.JsoupWithSaxon;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;

public final class ProbRefsIT extends TestCase {
    private String problem;

    @Override
    public void setUp() throws Exception {
        this.problem = Files.readString(
            new File(
                "src/test/resources/pages/problem.html"
            ).toPath()
        );
    }

    public void testForNbsp() {
        assertFalse(
            new ProbRefs(
                new JsoupWithSaxon(),
                new ProblemPage(
                    new Text.Of(this.problem)
                )
            ).content().contains("\u00A0")
        );
    }

    public void testOfTrimmedLines() {
        final var refs = new ProbRefs(
            new JsoupWithSaxon(),
            new ProblemPage(
                new Text.Of(this.problem)
            )
        ).content();
        assertEquals(
            Arrays.stream(refs.split("\n")).toList(),
            Arrays.stream(refs.split("\n")).map(String::trim).toList()
        );
    }

    public void testGoogleRef() {
        assertTrue(
            new ProbRefs(
                new JsoupWithSaxon(),
                new ProblemPage(
                    new Text.Of(this.problem)
                )
            ).content().startsWith("https://google.com")
        );
    }

    public void testNoInnerText() {
        final var refs = new ProbRefs(
            new JsoupWithSaxon(),
            new ProblemPage(
                new Text.Of(this.problem)
            )
        ).content();
        assertFalse(refs.contains("На стандартном потоке"));
    }

    public void testOnlyLinks() {
        final var refs = new ProbRefs(
            new JsoupWithSaxon(),
            new ProblemPage(
                new Text.Of(this.problem)
            )
        ).content();
        for (final String line : refs.split("\n")) {
            assertTrue(
                "Not a link: " + line,
                line.startsWith("http://") || line.startsWith("https://")
            );
        }
    }

    public void testFullListOfRefs() {
        assertEquals(
            """
            https://google.com
            https://youtube.com
            https://apple.com""",
            new ProbRefs(
                new JsoupWithSaxon(),
                new ProblemPage(
                    new Text.Of(this.problem)
                )
            ).content()
        );
    }

    public void testWrongPage() {
        assertTrue(
            new ProbRefs(
                new JsoupWithSaxon(),
                new ProblemPage(
                    new Text.Of(
                        """
                        <html>
                            <body>
                                <p>404 Not found</p>
                            </body>
                        </html>
                        """
                    )
                )
            ).content().isEmpty()
        );
    }

    public void testBrokenPage() {
        try {
            new ProbRefs(
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
            new ProbRefs(
                (xml, path) -> {
                    throw new InvariantViolation("There is no engine.");
                },
                new ProblemPage(
                    new Text.Of(this.problem)
                )
            ).content();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }
}
