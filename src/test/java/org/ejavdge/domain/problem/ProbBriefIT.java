package org.ejavdge.domain.problem;

import junit.framework.TestCase;
import org.ejavdge.contest.ProblemPage;
import org.ejavdge.dom.engine.JsoupWithSaxon;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;

public final class ProbBriefIT extends TestCase {
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
            new ProbBrief(
                new JsoupWithSaxon(),
                new ProblemPage(
                    new Text.Of(this.problem)
                )
            ).content().contains("\u00A0")
        );
    }

    public void testOfTrimmedLines() {
        final var brief = new ProbBrief(
            new JsoupWithSaxon(),
            new ProblemPage(
                new Text.Of(this.problem)
            )
        ).content();
        assertEquals(
            Arrays.stream(brief.split("\n")).toList(),
            Arrays.stream(brief.split("\n")).map(String::trim).toList()
        );
    }

    public void testBriefBase() {
        assertTrue(
            new ProbBrief(
                new JsoupWithSaxon(),
                new ProblemPage(
                    new Text.Of(this.problem)
                )
            ).content().startsWith(
                """
                Submit a solution for WithLinks
                with file
                На стандартном потоке ввода задаются два целых числа, не меньшие
                -32000 и не большие 32000.
                На стандартный поток вывода напечатайте сумму этих чисел.
                
                google
                attachment
                
                Числа задаются по одному в строке. Пробельные символы перед числом и после
                него отсутствуют. Пустые строки в вводе отсутствуют.
                
                youtube
                apple"""
            )
        );
    }

    public void testInnerLinks() {
        assertFalse(
            new ProbBrief(
                new JsoupWithSaxon(),
                new ProblemPage(
                    new Text.Of(this.problem)
                )
            ).content().endsWith(
                """
                https://google.com
                https://youtube.com
                https://apple.com"""
            )
        );
    }

    public void testFullBrief() {
        assertEquals(
        """
            Submit a solution for WithLinks
            with file
            На стандартном потоке ввода задаются два целых числа, не меньшие
            -32000 и не большие 32000.
            На стандартный поток вывода напечатайте сумму этих чисел.
            
            google
            attachment
            
            Числа задаются по одному в строке. Пробельные символы перед числом и после
            него отсутствуют. Пустые строки в вводе отсутствуют.
            
            youtube
            apple
            Examples
            Input
            1
            2
            
            Output
            3
            
            Input
            4
            5
            
            Output
            9
            
            """,
            new ProbBrief(
                new JsoupWithSaxon(),
                new ProblemPage(
                    new Text.Of(this.problem)
                )
            ).content()
        );
    }

    public void testWrongPage() {
        try {
            new ProbBrief(
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
            ).content();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testBrokenPage() {
        try {
            new ProbBrief(
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
            new ProbBrief(
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
