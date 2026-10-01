package org.ejavdge.domain.report;

import junit.framework.TestCase;
import org.ejavdge.app.setup.PresetEngine;
import org.ejavdge.auth.Session;
import org.ejavdge.contest.ContestResource;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.file.ByteFile;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.web.context.Location;
import org.ejavdge.web.driver.WebDriver;
import org.ejavdge.web.spec.Request;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.concurrent.atomic.AtomicInteger;

public final class LastReportIT extends TestCase {
    public void testLastReportContent() {
        try {
            final var main = Files.readString(
                new File(
                    "src/test/resources/pages/main.html"
                ).toPath()
            );
            final var problem = Files.readString(
                new File(
                    "src/test/resources/pages/problem.html"
                ).toPath()
            );
            final var report = Files.readString(
                new File(
                    "src/test/resources/pages/report.html"
                ).toPath()
            );
            final WebDriver driver = (
                final Location loc,
                final Request req
            ) -> {
                final var request = new String(
                    req.bytes(),
                    StandardCharsets.UTF_8
                );
                if (request.contains("action=37")) {
                    return report.getBytes(StandardCharsets.UTF_8);
                }
                if (request.contains("action=139")) {
                    return problem.getBytes(StandardCharsets.UTF_8);
                }
                return main.getBytes(StandardCharsets.UTF_8);
            };
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


                Result: OK
                Result: OK
                Result: OK

                       \s
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
                checker: { utime=0, stime=1, ptime=1, rtime=2, maxvsz=430080, maxrss=2097152, nvcsw=1, nivcsw=0 }""",
                new LastReport(
                    new ByteFile.Of(
                        new Text.Of("problem"),
                        new Bytes.Of(
                            "// problem: WithLinks\n".getBytes(StandardCharsets.UTF_8)
                        )
                    ),
                    new PresetEngine(),
                    new ContestResource(
                        driver,
                        new Location(
                            new Text.Of("/ejudge"),
                            new Text.Of("localhost"),
                            new Num.Of(90)
                        ),
                        new Session(
                            new Bytes.Of(
                                """
                                HTTP/1.1 302 FOUND\r
                                Set-Cookie: EJSID=756b423a0a6fe6a7;\r
                                Location: http://localhost:90/ejudge?SID=1684bb4a0f94302c&action=2&lt=1\r
                                Content-Length: 2\r
                                \r
                                OK\r
                                """.getBytes(StandardCharsets.UTF_8)
                            )
                        )
                    )
                ).content()
            );
        } catch (final IOException e) {
            throw new AssertionError(e);
        } catch (final InvariantViolation e) {
            fail(e.getMessage());
        }
    }

    public void testLastRunInfoWhenReportIsNotAvailable() {
        try {
            final var main = Files.readString(
                new File(
                    "src/test/resources/pages/main.html"
                ).toPath()
            );
            final var problem = Files.readString(
                new File(
                    "src/test/resources/pages/problem.html"
                ).toPath()
            );
            final WebDriver driver = (
                final Location loc,
                final Request req
            ) -> {
                final var request = new String(
                    req.bytes(),
                    StandardCharsets.UTF_8
                );
                if (request.contains("action=37")) {
                    return """
                        <html>
                            <body>
                                <p>404 Not found</p>
                            </body>
                        </html>
                        """.getBytes(StandardCharsets.UTF_8);
                }
                if (request.contains("action=139")) {
                    return problem.getBytes(StandardCharsets.UTF_8);
                }
                return main.getBytes(StandardCharsets.UTF_8);
            };
            assertEquals(
                """
                Run ID: 12
                Time: 495:41:05
                Size: 526
                Problem: WithLinks
                Language: r
                Result: Check failed
                Failed test: N/A
                View source: View
                View report: N/A""",
                new LastReport(
                    new ByteFile.Of(
                        new Text.Of("problem"),
                        new Bytes.Of(
                            "// problem: WithLinks\n".getBytes(StandardCharsets.UTF_8)
                        )
                    ),
                    new PresetEngine(),
                    new ContestResource(
                        driver,
                        new Location(
                            new Text.Of("/ejudge"),
                            new Text.Of("localhost"),
                            new Num.Of(90)
                        ),
                        new Session(
                            new Bytes.Of(
                                """
                                HTTP/1.1 302 FOUND\r
                                Set-Cookie: EJSID=756b423a0a6fe6a7;\r
                                Location: http://localhost:90/ejudge?SID=1684bb4a0f94302c&action=2&lt=1\r
                                Content-Length: 2\r
                                \r
                                OK\r
                                """.getBytes(StandardCharsets.UTF_8)
                            )
                        )
                    )
                ).content()
            );
        } catch (final IOException e) {
            throw new AssertionError(e);
        } catch (final InvariantViolation e) {
            fail(e.getMessage());
        }
    }

    public void testProblemPageCalls() {
        try {
            final var main = Files.readString(
                new File(
                    "src/test/resources/pages/main.html"
                ).toPath()
            );
            final var problem = Files.readString(
                new File(
                    "src/test/resources/pages/problem.html"
                ).toPath()
            );
            final var report = Files.readString(
                new File(
                    "src/test/resources/pages/report.html"
                ).toPath()
            );
            final var calls = new AtomicInteger(0);
            new LastReport(
                new ByteFile.Of(
                    new Text.Of("problem"),
                    new Bytes.Of(
                        "// problem: WithLinks\n".getBytes(StandardCharsets.UTF_8)
                    )
                ),
                new PresetEngine(),
                new ContestResource(
                    (loc, req) -> {
                        final var request = new String(req.bytes(), StandardCharsets.UTF_8);
                        if (request.contains("action=37")) {
                            return report.getBytes(StandardCharsets.UTF_8);
                        }
                        if (request.contains("action=139")) {
                            calls.incrementAndGet();
                            return problem.getBytes(StandardCharsets.UTF_8);
                        }
                        return main.getBytes(StandardCharsets.UTF_8);
                    },
                    new Location(
                        new Text.Of("/ejudge"),
                        new Text.Of("localhost"),
                        new Num.Of(90)
                    ),
                    new Session(
                        new Bytes.Of(
                            """
                            HTTP/1.1 302 FOUND\r
                            Set-Cookie: EJSID=756b423a0a6fe6a7;\r
                            Location: http://localhost:90/ejudge?SID=1684bb4a0f94302c&action=2&lt=1\r
                            Content-Length: 2\r
                            \r
                            OK\r
                            """.getBytes(StandardCharsets.UTF_8)
                        )
                    )
                )
            ).content();
            assertEquals(1, calls.get());
        } catch (final IOException e) {
            throw new AssertionError(e);
        } catch (final InvariantViolation e) {
            fail(e.getMessage());
        }
    }

    public void testMainPageCalls() {
        try {
            final var main = Files.readString(
                new File(
                    "src/test/resources/pages/main.html"
                ).toPath()
            );
            final var problem = Files.readString(
                new File(
                    "src/test/resources/pages/problem.html"
                ).toPath()
            );
            final var report = Files.readString(
                new File(
                    "src/test/resources/pages/report.html"
                ).toPath()
            );
            final var calls = new AtomicInteger(0);
            new LastReport(
                new ByteFile.Of(
                    new Text.Of("problem"),
                    new Bytes.Of(
                        "// problem: WithLinks\n".getBytes(StandardCharsets.UTF_8)
                    )
                ),
                new PresetEngine(),
                new ContestResource(
                    (loc, req) -> {
                        final var request = new String(req.bytes(), StandardCharsets.UTF_8);
                        if (request.contains("action=37")) {
                            return report.getBytes(StandardCharsets.UTF_8);
                        }
                        if (request.contains("action=139")) {
                            return problem.getBytes(StandardCharsets.UTF_8);
                        }
                        calls.incrementAndGet();
                        return main.getBytes(StandardCharsets.UTF_8);
                    },
                    new Location(
                        new Text.Of("/ejudge"),
                        new Text.Of("localhost"),
                        new Num.Of(90)
                    ),
                    new Session(
                        new Bytes.Of(
                            """
                            HTTP/1.1 302 FOUND\r
                            Set-Cookie: EJSID=756b423a0a6fe6a7;\r
                            Location: http://localhost:90/ejudge?SID=1684bb4a0f94302c&action=2&lt=1\r
                            Content-Length: 2\r
                            \r
                            OK\r
                            """.getBytes(StandardCharsets.UTF_8)
                        )
                    )
                )
            ).content();
            assertEquals(1, calls.get());
        } catch (final IOException e) {
            throw new AssertionError(e);
        } catch (final InvariantViolation e) {
            fail(e.getMessage());
        }
    }

    public void testReportPageCalls() {
        try {
            final var main = Files.readString(
                new File(
                    "src/test/resources/pages/main.html"
                ).toPath()
            );
            final var problem = Files.readString(
                new File(
                    "src/test/resources/pages/problem.html"
                ).toPath()
            );
            final var report = Files.readString(
                new File(
                    "src/test/resources/pages/report.html"
                ).toPath()
            );
            final var calls = new AtomicInteger(0);
            new LastReport(
                new ByteFile.Of(
                    new Text.Of("problem"),
                    new Bytes.Of(
                        "// problem: WithLinks\n".getBytes(StandardCharsets.UTF_8)
                    )
                ),
                new PresetEngine(),
                new ContestResource(
                    (loc, req) -> {
                        final var request = new String(req.bytes(), StandardCharsets.UTF_8);
                        if (request.contains("action=37")) {
                            calls.incrementAndGet();
                            return report.getBytes(StandardCharsets.UTF_8);
                        }
                        if (request.contains("action=139")) {
                            return problem.getBytes(StandardCharsets.UTF_8);
                        }
                        return main.getBytes(StandardCharsets.UTF_8);
                    },
                    new Location(
                        new Text.Of("/ejudge"),
                        new Text.Of("localhost"),
                        new Num.Of(90)
                    ),
                    new Session(
                        new Bytes.Of(
                            """
                            HTTP/1.1 302 FOUND\r
                            Set-Cookie: EJSID=756b423a0a6fe6a7;\r
                            Location: http://localhost:90/ejudge?SID=1684bb4a0f94302c&action=2&lt=1\r
                            Content-Length: 2\r
                            \r
                            OK\r
                            """.getBytes(StandardCharsets.UTF_8)
                        )
                    )
                )
            ).content();
            assertEquals(1, calls.get());
        } catch (final IOException e) {
            throw new AssertionError(e);
        } catch (final InvariantViolation e) {
            fail(e.getMessage());
        }
    }

    public void testInvalidSession() {
        try {
            final var main = Files.readString(
                new File(
                    "src/test/resources/pages/main.html"
                ).toPath()
            );
            final var problem = Files.readString(
                new File(
                    "src/test/resources/pages/problem.html"
                ).toPath()
            );
            final var report = Files.readString(
                new File(
                    "src/test/resources/pages/report.html"
                ).toPath()
            );
            new LastReport(
                new ByteFile.Of(
                    new Text.Of("problem"),
                    new Bytes.Of(
                        "// problem: WithLinks\n".getBytes(StandardCharsets.UTF_8)
                    )
                ),
                new PresetEngine(),
                new ContestResource(
                    (loc, req) -> {
                        final var request = new String(req.bytes(), StandardCharsets.UTF_8);
                        if (request.contains("action=37")) {
                            return report.getBytes(StandardCharsets.UTF_8);
                        }
                        if (request.contains("action=139")) {
                            return problem.getBytes(StandardCharsets.UTF_8);
                        }
                        return main.getBytes(StandardCharsets.UTF_8);
                    },
                    new Location(
                        new Text.Of("/ejudge"),
                        new Text.Of("localhost"),
                        new Num.Of(90)
                    ),
                    new Session(
                        new Bytes.Of(
                            """
                            HTTP/1.1 200 OK\r
                            Location: http://localhost:90/ejudge&action=2&lt=1\r
                            Content-Length: 15\r
                            \r
                            Invalid session\r
                            """.getBytes(StandardCharsets.UTF_8)
                        )
                    )
                )
            ).content();
        } catch (final InvariantViolation e) {
            return;
        } catch (final IOException e) {
            throw new AssertionError(e);
        }
        fail("InvariantViolation");
    }

    public void testBrokenDriver() {
        try {
            new LastReport(
                new ByteFile.Of(
                    new Text.Of("problem"),
                    new Bytes.Of(
                        "// problem: WithLinks\n".getBytes(StandardCharsets.UTF_8)
                    )
                ),
                new PresetEngine(),
                new ContestResource(
                    (loc, req) -> {
                        throw new InvariantViolation("there is no resources.");
                    },
                    new Location(
                        new Text.Of("/ejudge"),
                        new Text.Of("localhost"),
                        new Num.Of(90)
                    ),
                    new Session(
                        new Bytes.Of(
                            """
                            HTTP/1.1 302 FOUND\r
                            Set-Cookie: EJSID=756b423a0a6fe6a7;\r
                            Location: http://localhost:90/ejudge?SID=1684bb4a0f94302c&action=2&lt=1\r
                            Content-Length: 2\r
                            \r
                            OK\r
                            """.getBytes(StandardCharsets.UTF_8)
                        )
                    )
                )
            ).content();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testInvalidFile() {
        try {
            final var main = Files.readString(
                new File(
                    "src/test/resources/pages/main.html"
                ).toPath()
            );
            final var problem = Files.readString(
                new File(
                    "src/test/resources/pages/problem.html"
                ).toPath()
            );
            final var report = Files.readString(
                new File(
                    "src/test/resources/pages/report.html"
                ).toPath()
            );
            new LastReport(
                new ByteFile.Of(
                    new Text.Of("problem"),
                    new Bytes.Of(
                        "// lang: 14\n".getBytes(StandardCharsets.UTF_8)
                    )
                ),
                new PresetEngine(),
                new ContestResource(
                    (loc, req) -> {
                        final var request = new String(req.bytes(), StandardCharsets.UTF_8);
                        if (request.contains("action=37")) {
                            return report.getBytes(StandardCharsets.UTF_8);
                        }
                        if (request.contains("action=139")) {
                            return problem.getBytes(StandardCharsets.UTF_8);
                        }
                        return main.getBytes(StandardCharsets.UTF_8);
                    },
                    new Location(
                        new Text.Of("/ejudge"),
                        new Text.Of("localhost"),
                        new Num.Of(90)
                    ),
                    new Session(
                        new Bytes.Of(
                            """
                            HTTP/1.1 302 FOUND\r
                            Set-Cookie: EJSID=756b423a0a6fe6a7;\r
                            Location: http://localhost:90/ejudge?SID=1684bb4a0f94302c&action=2&lt=1\r
                            Content-Length: 2\r
                            \r
                            OK\r
                            """.getBytes(StandardCharsets.UTF_8)
                        )
                    )
                )
            ).content();
        } catch (final InvariantViolation e) {
            return;
        } catch (final IOException e) {
            throw new AssertionError(e);
        }
        fail("InvariantViolation");
    }
}
