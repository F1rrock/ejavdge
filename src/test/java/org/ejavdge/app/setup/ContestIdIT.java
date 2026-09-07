package org.ejavdge.app.setup;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public final class ContestIdIT extends TestCase {
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

    public void testValidId() {
        try {
            Files.writeString(
                this.tempFile.toPath(),
                """
                CONTEST_ID=1
                """
            );
        } catch (final IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(
            1,
            new ContestId(
                new Text.Of(this.tempFile.getParent()),
                new Text.Of(this.tempFile.getName())
            ).value()
        );
    }

    public void testNonNumericId() {
        try {
            Files.writeString(
                this.tempFile.toPath(),
                """
                CONTEST_ID=abc
                """
            );
        } catch (final IOException e) {
            throw new AssertionError(e);
        }
        try {
            new ContestId(
                new Text.Of(this.tempFile.getParent()),
                new Text.Of(this.tempFile.getName())
            ).value();
        } catch (InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testZeroId() {
        try {
            Files.writeString(
                this.tempFile.toPath(),
                """
                CONTEST_ID=0
                """
            );
        } catch (final IOException e) {
            throw new AssertionError(e);
        }
        try {
            new ContestId(
                new Text.Of(this.tempFile.getParent()),
                new Text.Of(this.tempFile.getName())
            ).value();
        } catch (InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testNegativeId() {
        try {
            Files.writeString(
                this.tempFile.toPath(),
                """
                CONTEST_ID=-1
                """
            );
        } catch (final IOException e) {
            throw new AssertionError(e);
        }
        try {
            new ContestId(
                new Text.Of(this.tempFile.getParent()),
                new Text.Of(this.tempFile.getName())
            ).value();
        } catch (InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testEmptyId() {
        try {
            Files.writeString(
                this.tempFile.toPath(),
                """
                CONTEST_ID=
                """
            );
        } catch (final IOException e) {
            throw new AssertionError(e);
        }
        try {
            new ContestId(
                new Text.Of(this.tempFile.getParent()),
                new Text.Of(this.tempFile.getName())
            ).value();
        } catch (InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testWithoutId() {
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
            new ContestId(
                new Text.Of(this.tempFile.getParent()),
                new Text.Of(this.tempFile.getName())
            ).value();
        } catch (InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testIdFromUnknownFile() {
        try {
            new ContestId(
                new Text.Of("path/to/non-existing-file"),
                new Text.Of("i-am-not-real.txt")
            ).value();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }
}
