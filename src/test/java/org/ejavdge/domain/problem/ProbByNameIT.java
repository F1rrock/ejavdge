package org.ejavdge.domain.problem;

import junit.framework.TestCase;
import org.ejavdge.contest.MainPage;
import org.ejavdge.dom.engine.JsoupWithSaxon;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

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
}
