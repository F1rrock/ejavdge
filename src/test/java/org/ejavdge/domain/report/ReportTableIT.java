package org.ejavdge.domain.report;

import junit.framework.TestCase;
import org.ejavdge.contest.ReportPage;
import org.ejavdge.dom.engine.JsoupWithSaxon;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

import java.io.File;
import java.nio.file.Files;

public final class ReportTableIT extends TestCase {
    private String report;

    @Override
    public void setUp() throws Exception {
        this.report = Files.readString(
            new File(
                "src/test/resources/pages/report.html"
            ).toPath()
        );
    }

    public void testContentsOfTable() {
        assertEquals(
            """
            Result: OK
            Result: OK
            Result: OK""",
            new ReportTable(
                new JsoupWithSaxon(),
                new ReportPage(
                    new Text.Of(this.report)
                )
            ).content()
        );
    }

    public void testWrongPage() {
        assertTrue(
            new ReportTable(
                new JsoupWithSaxon(),
                new ReportPage(
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
            new ReportTable(
                new JsoupWithSaxon(),
                new ReportPage(
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
            new ReportTable(
                (xml, path) -> {
                    throw new InvariantViolation("There is no engine.");
                },
                new ReportPage(
                    new Text.Of(this.report)
                )
            ).content();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }
}
