package org.ejavdge.app.setup;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public final class LoginIT extends TestCase {
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

    public void testValidLogin() {
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
        assertEquals(
            "login",
            new Login(
                new Text.Of(this.tempFile.getParent()),
                new Text.Of(this.tempFile.getName())
            ).content()
        );
    }

    public void testEmptyLogin() {
        try {
            Files.writeString(
                this.tempFile.toPath(),
                """
                LOGIN=
                """
            );
        } catch (final IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(
            "",
            new Login(
                new Text.Of(this.tempFile.getParent()),
                new Text.Of(this.tempFile.getName())
            ).content()
        );
    }

    public void testWithoutLogin() {
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
            new Login(
                new Text.Of(this.tempFile.getParent()),
                new Text.Of(this.tempFile.getName())
            ).content();
        } catch (InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testLoginFromUnknownFile() {
        try {
            new Login(
                new Text.Of("path/to/non-existing-file"),
                new Text.Of("i-am-not-real.txt")
            ).content();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }
}
