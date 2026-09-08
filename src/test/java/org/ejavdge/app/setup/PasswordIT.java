package org.ejavdge.app.setup;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public final class PasswordIT extends TestCase {
    private File tempFile;

    @Override
    protected void setUp() throws IOException {
        this.tempFile = File.createTempFile(".env", ".tmp");
    }

    @Override
    protected void tearDown() {
        if (this.tempFile != null && this.tempFile.exists()) {
            this.tempFile.deleteOnExit();
        }
    }

    public void testValidPassword() {
        try {
            Files.writeString(
                this.tempFile.toPath(),
                """
                PASSWORD=pass
                """
            );
        } catch (final IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(
            "pass",
            new Password(
                new Text.Of(this.tempFile.getParent()),
                new Text.Of(this.tempFile.getName())
            ).content()
        );
    }

    public void testEmptyPassword() {
        try {
            Files.writeString(
                this.tempFile.toPath(),
                """
                PASSWORD=
                """
            );
        } catch (final IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(
            "",
            new Password(
                new Text.Of(this.tempFile.getParent()),
                new Text.Of(this.tempFile.getName())
            ).content()
        );
    }

    public void testWithoutPassword() {
        try {
            Files.writeString(
                this.tempFile.toPath(),
                """
                KEY=value
                """
            );
        } catch (final IOException e) {
            throw new AssertionError(e);
        }
        try {
            new Password(
                new Text.Of(this.tempFile.getParent()),
                new Text.Of(this.tempFile.getName())
            ).content();
        } catch (InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testPasswordFromUnknownFile() {
        try {
            new Password(
                new Text.Of("path/to/non-existing-file"),
                new Text.Of("i-am-not-real.txt")
            ).content();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }
}
