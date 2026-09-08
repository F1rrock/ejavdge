package org.ejavdge.domain.problem;

import junit.framework.TestCase;
import org.ejavdge.contest.MainPage;
import org.ejavdge.dom.engine.JsoupWithSaxon;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public final class ProbCatalogIT extends TestCase {
    public void testEjudgePage() {
        try {
            assertEquals(
                "A, B, WithLinks",
                new ProbCatalog(
                    new JsoupWithSaxon(),
                    new MainPage(
                        new Text.Of(
                            Files.readString(
                                new File(
                                    "src/test/resources/pages/main.html"
                                ).toPath()
                            )
                        )
                    )
                ).content()
            );
        } catch (final IOException e) {
            throw new AssertionError(e);
        }
    }

    public void testWrongPage() {
        try {
            new ProbCatalog(
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
                )
            ).content();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testBrokenPage() {
        try {
            new ProbCatalog(
                (xml, path) ->  xml.content(),
                new MainPage(
                    () -> {
                        throw new InvariantViolation("There is no text");
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
            new ProbCatalog(
                (xml, path) -> {
                    throw new InvariantViolation("There is no valid XML");
                },
                new MainPage(
                    new Text.Of("fake")
                )
            ).content();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }
}
