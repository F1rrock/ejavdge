package org.ejavdge.app;

import junit.framework.TestCase;
import org.ejavdge.auth.Session;
import org.ejavdge.contest.ContestResource;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.file.ByteFile;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.web.context.Location;
import org.ejavdge.web.driver.WebDriver;
import org.ejavdge.web.driver.WithLogsDriver;
import org.ejavdge.web.spec.Request;
import org.ejavdge.workspace.out.Out;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.concurrent.atomic.AtomicInteger;

public final class ProblemDescriptionIT extends TestCase {
    public void testProblemDescription() {
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
                if (request.contains("prob_id=3")) {
                    return problem.getBytes(StandardCharsets.UTF_8);
                }
                return main.getBytes(StandardCharsets.UTF_8);
            };
            final var output = new StringBuilder();
            final Out out = text -> output.append(text.content());
            new ProblemDescription(
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
                ),
                new ByteFile.Of(
                    new Text.Of("problem"),
                    new Bytes.Of(
                        "// problem: WithLinks\n".getBytes(StandardCharsets.UTF_8)
                    )
                ),
                out
            ).run();
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
                
                
                Used references:
                https://google.com
                https://example.com
                https://youtube.com
                https://apple.com""",
                output.toString()
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
            final var calls = new AtomicInteger(0);
            new ProblemDescription(
                new ContestResource(
                    (loc, req) -> {
                        if (new String(req.bytes(), StandardCharsets.UTF_8).contains("prob_id=3")) {
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
                ),
                new ByteFile.Of(
                    new Text.Of("problem"),
                    new Bytes.Of(
                        "// problem: WithLinks\n".getBytes(StandardCharsets.UTF_8)
                    )
                ),
                Text::content
            ).run();
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
            final var calls = new AtomicInteger(0);
            new ProblemDescription(
                new ContestResource(
                    (loc, req) -> {
                        if (new String(req.bytes(), StandardCharsets.UTF_8).contains("prob_id=3")) {
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
                ),
                new ByteFile.Of(
                    new Text.Of("problem"),
                    new Bytes.Of(
                        "// problem: WithLinks\n".getBytes(StandardCharsets.UTF_8)
                    )
                ),
                Text::content
            ).run();
            assertEquals(1, calls.get());
        } catch (final IOException e) {
            throw new AssertionError(e);
        } catch (final InvariantViolation e) {
            fail(e.getMessage());
        }
    }

    public void testWithLogsMainPageCalls() {
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
            final var calls = new AtomicInteger(0);
            new ProblemDescription(
                new ContestResource(
                    new WithLogsDriver(
                        (loc, req) -> {
                            if (new String(req.bytes(), StandardCharsets.UTF_8).contains("prob_id=3")) {
                                return problem.getBytes(StandardCharsets.UTF_8);
                            }
                            calls.incrementAndGet();
                            return main.getBytes(StandardCharsets.UTF_8);
                        },
                        new FakeLogger()
                    ),
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
                ),
                new ByteFile.Of(
                    new Text.Of("problem"),
                    new Bytes.Of(
                        "// problem: WithLinks\n".getBytes(StandardCharsets.UTF_8)
                    )
                ),
                Text::content
            ).run();
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
            new ProblemDescription(
                new ContestResource(
                    new WithLogsDriver(
                        (loc, req) -> {
                            if (new String(req.bytes(), StandardCharsets.UTF_8).contains("prob_id=3")) {
                                return problem.getBytes(StandardCharsets.UTF_8);
                            }
                            return main.getBytes(StandardCharsets.UTF_8);
                        },
                        new FakeLogger()
                    ),
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
                ),
                new ByteFile.Of(
                    new Text.Of("problem"),
                    new Bytes.Of(
                        "// problem: WithLinks\n".getBytes(StandardCharsets.UTF_8)
                    )
                ),
                Text::content
            ).run();
        } catch (final InvariantViolation e) {
            return;
        } catch (final IOException e) {
            throw new AssertionError(e);
        }
        fail("InvariantViolation");
    }

    public void testBrokenDriver() {
        try {
            new ProblemDescription(
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
                ),
                new ByteFile.Of(
                    new Text.Of("problem"),
                    new Bytes.Of(
                        "// problem: WithLinks\n".getBytes(StandardCharsets.UTF_8)
                    )
                ),
                Text::content
            ).run();
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
            new ProblemDescription(
                new ContestResource(
                    new WithLogsDriver(
                        (loc, req) -> {
                            if (new String(req.bytes(), StandardCharsets.UTF_8).contains("prob_id=3")) {
                                return problem.getBytes(StandardCharsets.UTF_8);
                            }
                            return main.getBytes(StandardCharsets.UTF_8);
                        },
                        new FakeLogger()
                    ),
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
                ),
                new ByteFile.Of(
                    new Text.Of("problem"),
                    new Bytes.Of(
                        "// lang: 14\n".getBytes(StandardCharsets.UTF_8)
                    )
                ),
                Text::content
            ).run();
        } catch (final InvariantViolation e) {
            return;
        } catch (final IOException e) {
            throw new AssertionError(e);
        }
        fail("InvariantViolation");
    }
}
