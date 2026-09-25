package org.ejavdge.domain.problem;

import junit.framework.TestCase;
import org.ejavdge.contest.MainPage;
import org.ejavdge.dom.engine.JsoupWithSaxon;
import org.ejavdge.domain.solution.ProbNameOf;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.file.ByteFile;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.text.Text;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.concurrent.atomic.AtomicInteger;

public final class ProbByNameIT extends TestCase {
    private void testProblem(final String s, final int n) {
        try {
            assertEquals(
                n,
                new ProbByName(
                    new JsoupWithSaxon(),
                    new MainPage(
                        new Text.Of(
                            Files.readString(
                                new File(
                                    "src/test/resources/pages/main.html"
                                ).toPath()
                            )
                        )
                    ),
                    new Text.Of(s)
                ).value()
            );
        } catch (final IOException e) {
            throw new AssertionError(e);
        }
    }

    public void testProblemA() {
        this.testProblem("A", 1);
    }

    public void testProblemB() {
        this.testProblem("B", 2);
    }

    public void testProblemWithLinks() {
        this.testProblem("WithLinks", 3);
    }

    public void testUnknownProblem() {
        try {
            this.testProblem("UnknownProblem", 0);
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testWrongPage() {
        try {
            new ProbByName(
                new JsoupWithSaxon(),
                new MainPage(
                    new Text.Of(
                        """
                        <html>
                            <body>
                                <p>404 Not found</p>
                            </body>
                        </html>
                        """
                    )
                ),
                new Text.Of("A")
            ).value();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testBrokenPage() {
        try {
            new ProbByName(
                (xml, path) ->  xml.content(),
                new MainPage(
                    () -> {
                        throw new InvariantViolation("There is no text");
                    }
                ),
                new Text.Of("A")
            ).value();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testBrokenEngine() {
        try {
            new ProbByName(
                (xml, path) -> {
                    throw new InvariantViolation("There is no valid XML");
                },
                new MainPage(
                    new Text.Of("fake")
                ),
                new Text.Of("A")
            ).value();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testBrokenName() {
        try {
            new ProbByName(
                (xml, path) -> xml.content(),
                new MainPage(
                    new Text.Of("fake")
                ),
                () -> {
                    throw new InvariantViolation("There is no text");
                }
            ).value();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testMainPageCalls() {
        final var calls = new AtomicInteger(0);
        new ProbByName(
            new JsoupWithSaxon(),
            new MainPage(
                () -> {
                    try {
                        calls.incrementAndGet();
                        return Files.readString(
                            new File(
                                "src/test/resources/pages/main.html"
                            ).toPath()
                        );
                    } catch (final IOException e) {
                        throw new AssertionError(e);
                    }
                }
            ),
            new Text.Of("A")
        ).value();
        assertEquals(1, calls.get());
    }

    public void testFileWithProblemMarker() {
        try {
            new ProbByName(
                new JsoupWithSaxon(),
                new MainPage(
                    new Text.Of(
                        """
                        <html>
                            <body>
                                <ul id="probNavTopList">
                                    <tr>
                                        <a class="tab" href="prob_id=1">A</a>
                                    </tr>
                                </ul>
                            </body>
                        </html>
                        """
                    )
                ),
                new ProbNameOf(
                    new ByteFile.Of(
                        new Text.Of("WithoutMarker.java"),
                        new Bytes.Of(
                            """
                            public class WithoutMarker {}
                            """.getBytes(StandardCharsets.UTF_8)
                        )
                    )
                )
            ).value();
        } catch (final InvariantViolation e) {
            return;
        }

        fail("InvariantViolation");
    }
}
