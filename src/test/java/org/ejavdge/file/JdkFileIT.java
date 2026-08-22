package org.ejavdge.file;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public final class JdkFileIT extends TestCase {
    private File tempFile;

    @Override
    protected void setUp() throws IOException {
        this.tempFile = File.createTempFile("test", ".txt");
    }

    @Override
    protected void tearDown() {
        if (this.tempFile != null && this.tempFile.exists()) {
            this.tempFile.deleteOnExit();
        }
    }

    public void testName() {
        assertEquals(
            this.tempFile.getName(),
            new JdkFile(this.tempFile).name()
        );
    }

    public void testContent() {
        final var expected = "Hello, world!";
        try {
            Files.writeString(
                this.tempFile.toPath(),
                expected
            );
        } catch (final IOException e) {
            fail(e.getMessage());
        }
        assertEquals(
            expected,
            new String(
                new JdkFile(this.tempFile).content(),
                StandardCharsets.UTF_8
            )
        );
    }

    public void testMissingFile() {
        try {
            new JdkFile(
                new File("/path/that/does/not/exist/file.txt")
            ).content();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }
}
