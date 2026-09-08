package org.ejavdge.app.setup;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public final class PortIT extends TestCase {
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

    public void testValidPort() {
        try {
            Files.writeString(
                this.tempFile.toPath(),
                """
                PORT=90
                """
            );
        } catch (final IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(
            90,
            new Port(
                new Text.Of(this.tempFile.getParent()),
                new Text.Of(this.tempFile.getName())
            ).value()
        );
    }

    public void testNonNumericPort() {
        try {
            Files.writeString(
                this.tempFile.toPath(),
                """
                PORT=abc
                """
            );
        } catch (final IOException e) {
            throw new AssertionError(e);
        }
        try {
            new Port(
                new Text.Of(this.tempFile.getParent()),
                new Text.Of(this.tempFile.getName())
            ).value();
        } catch (InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testZeroPort() {
        try {
            Files.writeString(
                this.tempFile.toPath(),
                """
                PORT=0
                """
            );
        } catch (final IOException e) {
            throw new AssertionError(e);
        }
        try {
            new Port(
                new Text.Of(this.tempFile.getParent()),
                new Text.Of(this.tempFile.getName())
            ).value();
        } catch (InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testNegativePort() {
        try {
            Files.writeString(
                this.tempFile.toPath(),
                """
                PORT=-90
                """
            );
        } catch (final IOException e) {
            throw new AssertionError(e);
        }
        try {
            new Port(
                new Text.Of(this.tempFile.getParent()),
                new Text.Of(this.tempFile.getName())
            ).value();
        } catch (InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testEmptyPort() {
        try {
            Files.writeString(
                this.tempFile.toPath(),
                """
                PORT=
                """
            );
        } catch (final IOException e) {
            throw new AssertionError(e);
        }
        try {
            new Port(
                new Text.Of(this.tempFile.getParent()),
                new Text.Of(this.tempFile.getName())
            ).value();
        } catch (InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testWithoutPort() {
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
            new Port(
                new Text.Of(this.tempFile.getParent()),
                new Text.Of(this.tempFile.getName())
            ).value();
        } catch (InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testPortFromUnknownFile() {
        try {
            new Port(
                new Text.Of("path/to/non-existing-file"),
                new Text.Of("i-am-not-real.txt")
            ).value();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }
}
