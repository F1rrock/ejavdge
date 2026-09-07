package org.ejavdge.app.setup;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public final class ClientPathIT extends TestCase {
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

    public void testValidClientPath() {
        try {
            Files.writeString(
                this.tempFile.toPath(),
                """
                CLIENT_PATH=/ejudge
                """
            );
        } catch (final IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(
            "/ejudge",
            new ClientPath(
                new Text.Of(this.tempFile.getParent()),
                new Text.Of(this.tempFile.getName())
            ).content()
        );
    }

    public void testRootClientPath() {
        try {
            Files.writeString(
                this.tempFile.toPath(),
                """
                CLIENT_PATH=/
                """
            );
        } catch (final IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(
            "/",
            new ClientPath(
                new Text.Of(this.tempFile.getParent()),
                new Text.Of(this.tempFile.getName())
            ).content()
        );
    }

    public void testEmptyClientPath() {
        try {
            Files.writeString(
                this.tempFile.toPath(),
                """
                CLIENT_PATH=
                """
            );
        } catch (final IOException e) {
            throw new AssertionError(e);
        }
        try {
            new ClientPath(
                new Text.Of(this.tempFile.getParent()),
                new Text.Of(this.tempFile.getName())
            ).content();
        } catch (InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testWithoutClientPath() {
        try {
            Files.writeString(
                this.tempFile.toPath(),
                """
                LOGIN=login
                """
            );
        } catch (final IOException e) {
            throw new AssertionError(e);
        }
        try {
            new ClientPath(
                new Text.Of(this.tempFile.getParent()),
                new Text.Of(this.tempFile.getName())
            ).content();
        } catch (InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testPathFromUnknownFile() {
        try {
            new ClientPath(
                new Text.Of("path/to/non-existing-file"),
                new Text.Of("i-am-not-real.txt")
            ).content();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }
}
