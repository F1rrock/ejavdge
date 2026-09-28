package org.ejavdge.domain.report;

import junit.framework.TestCase;
import org.ejavdge.contest.ReportPage;
import org.ejavdge.dom.engine.JsoupWithSaxon;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

import java.io.File;
import java.nio.file.Files;

public final class ReportHeaderIT extends TestCase {
    private String report;

    @Override
    public void setUp() throws Exception {
        this.report = Files.readString(
            new File(
                "src/test/resources/pages/report.html"
            ).toPath()
        );
    }

    public void testContentsOfHeader() {
        assertEquals(
            """
            OK
            Max. CPU time: 1.222
            Testing messages
            :
            run: ATTENTION: core file pattern in /proc/sys/kernel/core_pattern
            is set to pipe the core file to a helper program.
            This is NOT RECOMMENDED for correct judging.
            Please, modify the core_pattern file.
            For example, consider disabling abrtd.
            
            """,
            new ReportHeader(
                new JsoupWithSaxon(),
                new ReportPage(
                    new Text.Of(this.report)
                )
            ).content()
        );
    }

    public void testWrongPage() {
        assertTrue(
            new ReportHeader(
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
            new ReportHeader(
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
            new ReportHeader(
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
