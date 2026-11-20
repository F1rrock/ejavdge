package org.ejavdge.file;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.text.Text;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public final class JavaProgramIT extends TestCase {
    private File tempFile;

    @Override
    protected void setUp() throws IOException {
        this.tempFile = File.createTempFile("test", ".java");
    }

    @Override
    protected void tearDown() {
        try {
            Files.deleteIfExists(this.tempFile.toPath());
        } catch (final IOException e) {
            this.tempFile.deleteOnExit();
        }
    }

    public void testEcho() throws IOException {
        assertEquals(
            "hello",
            this.program(
                """
                public class Main {
                    public static void main(String[] args) throws Exception {
                        int c;
                        while ((c = System.in.read()) != -1) {
                            System.out.write(c);
                        }
                        System.out.flush();
                    }
                }
                """
            ).outcomeOf(new Text.Of("hello"))
        );
    }

    public void testSum() throws IOException {
        assertEquals(
            """
            3
            """,
            this.program(
                """
                import java.util.Scanner;

                public class Main {
                    public static void main(String[] args) {
                        Scanner s = new Scanner(System.in);
                        System.out.println(s.nextInt() + s.nextInt());
                    }
                }
                """
            ).outcomeOf(new Text.Of(
                """
                1
                2
                """
            ))
        );
    }

    public void testSeveralLines() throws IOException {
        assertEquals(
            """
            1
            2
            3
            """,
            this.program(
                """
                public class Main {
                    public static void main(String[] args) throws Exception {
                        int c;
                        while ((c = System.in.read()) != -1) {
                            System.out.write(c);
                        }
                        System.out.flush();
                    }
                }
                """
            ).outcomeOf(new Text.Of("1\n2\n3\n"))
        );
    }

    public void testEmptyInput() throws IOException {
        assertEquals(
            "",
            this.program(
                """
                public class Main {
                    public static void main(String[] args) {
                    }
                }
                """
            ).outcomeOf(new Text.Of(""))
        );
    }

    public void testCompilationError() throws IOException {
        try {
            this.program(
                """
                public class Main {
                    this is not java;
                }
                """
            ).outcomeOf(new Text.Of(""));
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testRuntimeError() throws IOException {
        try {
            this.program(
                """
                public class Main {
                    public static void main(String[] args) {
                        throw new RuntimeException("boom");
                    }
                }
                """
            ).outcomeOf(new Text.Of(""));
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testMissingFile() {
        try {
            new JavaProgram(
                new JdkFile(
                    new File("/path/that/does/not/exist/Main.java")
                ),
                new Text.Of(".")
            ).outcomeOf(new Text.Of(""));
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testReplacementSourceInOriginalWorkingDirectory() throws IOException {
        final var directory = Files.createTempDirectory("ejavdge-probe");
        final var input = directory.resolve("probe-input.txt");
        try {
            Files.writeString(input, "Hello from the working directory");
            final var source = "// problem: A\n";
            final var original = new JavaProgram(
                new ByteFile.Of(
                    new Text.Of("Solution.java"),
                    new Bytes.Of(source.getBytes(StandardCharsets.UTF_8))
                ),
                new Text.Of(directory.toString())
            );
            final var replacement = new Bytes.Of(
                """
                import java.nio.file.Files;
                import java.nio.file.Path;

                class Main {
                    public static void main(String[] args) throws Exception {
                        System.out.print(Files.readString(Path.of("probe-input.txt")));
                    }
                }
                """.getBytes(StandardCharsets.UTF_8)
            );
            assertEquals("Solution.java", new JavaProgram(original, replacement).name());
            assertEquals(
                "Hello from the working directory",
                new JavaProgram.WithSource(original, replacement).outcomeOf(new Text.Of(""))
            );
            assertEquals(source, new String(original.content(), StandardCharsets.UTF_8));
        } finally {
            Files.deleteIfExists(input);
            Files.deleteIfExists(directory);
        }
    }

    private JavaProgram program(final String source) throws IOException {
        Files.writeString(this.tempFile.toPath(), source);
        return new JavaProgram(new JdkFile(this.tempFile), new Text.Of("."));
    }
}
