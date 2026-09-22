package org.ejavdge.file;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.items.Items;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.web.driver.WebDriver;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public final class DownloadingOfIT extends TestCase {
    private Path dir;

    @Override
    protected void setUp() throws IOException {
        this.dir = Files.createTempDirectory("ejavdge");
    }

    @Override
    protected void tearDown() throws IOException {
        try (final var paths = Files.walk(this.dir)) {
            for (final var p : paths.sorted(Comparator.reverseOrder()).toList()) {
                Files.deleteIfExists(p);
            }
        }
    }

    public void testDownloadsSingleFile() throws IOException {
        new DownloadingOf(
            new Items.Of<>(
                new Text.Of("http://localhost:90/files/sample.txt")
            ),
            this.driver("hello"),
            new Text.Of(this.dir.toString())
        ).perform();
        assertEquals(
            "hello",
            Files.readString(this.dir.resolve("sample.txt"))
        );
    }

    public void testDownloadsSeveralFiles() throws IOException {
        new DownloadingOf(
            new Items.Of<>(
                new Text.Of("http://localhost:90/files/a.txt"),
                new Text.Of("http://localhost:90/files/b.txt")
            ),
            this.driver("aaa", "bbb"),
            new Text.Of(this.dir.toString())
        ).perform();
        assertTrue(
            Files.readString(this.dir.resolve("a.txt")).equals("aaa")
                && Files.readString(this.dir.resolve("b.txt")).equals("bbb")
        );
    }

    public void testCreatesDirectoryIfMissing() throws IOException {
        final var nested = this.dir.resolve("nested/deeper");
        new DownloadingOf(
            new Items.Of<>(
                new Text.Of("http://localhost:90/files/sample.txt")
            ),
            this.driver("hello"),
            new Text.Of(nested.toString())
        ).perform();
        assertEquals(
            "hello",
            Files.readString(nested.resolve("sample.txt"))
        );
    }

    public void testOverwritesExistingFile() throws IOException {
        Files.writeString(this.dir.resolve("sample.txt"), "old");
        new DownloadingOf(
            new Items.Of<>(
                new Text.Of("http://localhost:90/files/sample.txt")
            ),
            this.driver("new"),
            new Text.Of(this.dir.toString())
        ).perform();
        assertEquals(
            "new",
            Files.readString(this.dir.resolve("sample.txt"))
        );
    }

    public void testDecodesFileName() throws IOException {
        new DownloadingOf(
            new Items.Of<>(
                new Text.Of("http://localhost:90/files/my%20file.txt")
            ),
            this.driver("hello"),
            new Text.Of(this.dir.toString())
        ).perform();
        assertEquals(
            "hello",
            Files.readString(this.dir.resolve("my file.txt"))
        );
    }

    public void testEmptyUrls() throws IOException {
        new DownloadingOf(
            new Items.Of<>(),
            this.driver(),
            new Text.Of(this.dir.toString())
        ).perform();
        try (final var files = Files.list(this.dir)) {
            assertEquals(0, files.count());
        }
    }

    public void testDriverCalledOncePerFile() {
        final var calls = new AtomicInteger(0);
        new DownloadingOf(
            new Items.Of<>(
                new Text.Of("http://localhost:90/files/sample.txt")
            ),
            (loc, req) -> {
                calls.incrementAndGet();
                return "hello".getBytes(StandardCharsets.UTF_8);
            },
            new Text.Of(this.dir.toString())
        ).perform();
        assertEquals(1, calls.get());
    }

    public void testEmptyLastSegment() {
        try {
            new DownloadingOf(
                new Items.Of<>(
                    new Text.Of("http://localhost:90/")
                ),
                this.driver(),
                new Text.Of(this.dir.toString())
            ).perform();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testMalformedUrl() {
        try {
            new DownloadingOf(
                new Items.Of<>(
                    new Text.Of("ht tp://bad url")
                ),
                this.driver(),
                new Text.Of(this.dir.toString())
            ).perform();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testBrokenDriver() {
        try {
            new DownloadingOf(
                new Items.Of<>(
                    new Text.Of("http://localhost:90/files/sample.txt")
                ),
                (loc, req) -> {
                    throw new InvariantViolation("There is no resource.");
                },
                new Text.Of(this.dir.toString())
            ).perform();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testBrokenLinks() {
        try {
            new DownloadingOf(
                () -> {
                    throw new InvariantViolation("There are no links.");
                },
                this.driver(),
                new Text.Of(this.dir.toString())
            ).perform();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testPlusInFileName() {
        new DownloadingOf(
            new Items.Of<>(
                new Text.Of("http://localhost:90/files/file+1.txt")
            ),
            this.driver("hello"),
            new Text.Of(this.dir.toString())
        ).perform();
        assertTrue(
            Files.exists(this.dir.resolve("file 1.txt"))
        );
    }

    public void testCyrillicFileName() throws IOException {
        new DownloadingOf(
            new Items.Of<>(
                new Text.Of(
                    "http://localhost:90/files/%D0%BF%D1%80%D0%B8%D0%BC%D0%B5%D1%80.txt"
                )
            ),
            this.driver("hello"),
            new Text.Of(this.dir.toString())
        ).perform();
        assertEquals(
            "hello",
            Files.readString(this.dir.resolve("пример.txt"))
        );
    }

    public void testUrlWithQueryString() throws IOException {
        new DownloadingOf(
            new Items.Of<>(
                new Text.Of("http://localhost:90/files/sample.txt?id=42")
            ),
            this.driver("hello"),
            new Text.Of(this.dir.toString())
        ).perform();
        assertEquals(
            "hello",
            Files.readString(this.dir.resolve("sample.txt"))
        );
    }

    public void testPathIsNotDirectory() throws IOException {
        final var file = Files.createFile(this.dir.resolve("not-a-dir"));
        try {
            new DownloadingOf(
                new Items.Of<>(
                    new Text.Of("http://localhost:90/files/sample.txt")
                ),
                this.driver("hello"),
                new Text.Of(file.toString())
            ).perform();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testPartialFailure() {
        try {
            new DownloadingOf(
                new Items.Of<>(
                    new Text.Of("http://localhost:90/files/a.txt"),
                    new Text.Of("http://localhost:90/files/b.txt")
                ),
                (loc, req) -> {
                    throw new InvariantViolation("boom");
                },
                new Text.Of(this.dir.toString())
            ).perform();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    private WebDriver driver(final String... responses) {
        final var queue = new ArrayDeque<>(List.of(responses));
        return (loc, req) -> {
            if (queue.isEmpty()) {
                throw new InvariantViolation("Unexpected driver call");
            }
            return queue.poll().getBytes(StandardCharsets.UTF_8);
        };
    }
}
