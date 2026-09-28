package org.ejavdge.domain.report;

import junit.framework.TestCase;
import org.ejavdge.contest.ReportPage;
import org.ejavdge.dom.engine.JsoupWithSaxon;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

import java.io.File;
import java.nio.file.Files;

public final class ReportDetailsIT extends TestCase {
    private String report;

    @Override
    public void setUp() throws Exception {
        this.report = Files.readString(
            new File(
                "src/test/resources/pages/report.html"
            ).toPath()
        );
    }

    public void testDetailsOfReport() {
        assertEquals(
        """
            
            
            L
            Command-line parameters
            
                   \s
            I
            Test input
            
                   \s
            O
            Program output
            
                   \s
            A
            Correct output
            
                   \s
            E
            Program output to stderr
            
                   \s
            C
            Checker output
            
                   \s
            F
            Additional test information
            
                   \s
            ====== Test #1 =======
            
            
            --- Input: size 14 ---
            
            6
            1 2 3 4 5 6
            
            
            --- Output: size 11 ---
            
            1 3 5 6 4 2
            
            --- Correct: size 12 ---
            
            1 3 5 6 4 2
            
            
            --- Stderr: size 0 ---
            
            
            
            --- Checker output: size 3 ---
            
            OK
            
            
            --- Resource usage ---
            
            program: { utime=699, stime=523, ptime=1222, rtime=297, maxvsz=2440470528, maxrss=87969792, nvcsw=48, nivcsw=49 }
            checker: { utime=0, stime=1, ptime=1, rtime=2, maxvsz=430080, maxrss=2228224, nvcsw=1, nivcsw=0 }
            
            
            ====== Test #2 =======
            
            
            --- Input: size 18 ---
            
            5
            1.5 0 -2 7.25 3
            
            
            --- Output: size 15 ---
            
            1.5 -2 3 7.25 0
            
            --- Correct: size 16 ---
            
            1.5 -2 3 7.25 0
            
            
            --- Stderr: size 0 ---
            
            
            
            --- Checker output: size 3 ---
            
            OK
            
            
            --- Resource usage ---
            
            program: { utime=689, stime=491, ptime=1179, rtime=298, maxvsz=2371514368, maxrss=87764992, nvcsw=45, nivcsw=44 }
            checker: { utime=0, stime=1, ptime=1, rtime=1, maxvsz=430080, maxrss=2228224, nvcsw=1, nivcsw=0 }
            
            
            ====== Test #3 =======
            
            
            --- Input: size 8 ---
            
            2
            10 20
            
            
            --- Output: size 5 ---
            
            10 20
            
            --- Correct: size 6 ---
            
            10 20
            
            
            --- Stderr: size 0 ---
            
            
            
            --- Checker output: size 3 ---
            
            OK
            
            
            --- Resource usage ---
            
            program: { utime=720, stime=497, ptime=1217, rtime=291, maxvsz=2441179136, maxrss=87973888, nvcsw=46, nivcsw=37 }
            checker: { utime=0, stime=1, ptime=1, rtime=2, maxvsz=430080, maxrss=2097152, nvcsw=1, nivcsw=0 }
            
            """,
            new ReportDetails(
                new JsoupWithSaxon(),
                new ReportPage(
                    new Text.Of(this.report)
                )
            ).content()
        );
    }

    public void testWrongPage() {
        assertTrue(
            new ReportDetails(
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
            new ReportDetails(
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
            new ReportDetails(
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
