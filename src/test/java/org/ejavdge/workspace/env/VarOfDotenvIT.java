package org.ejavdge.workspace.env;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public final class VarOfDotenvIT extends TestCase {
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

    public void testValueOfExistingVar() {
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
            new VarOfDotenv(
                "LOGIN",
                this.tempFile.getParent(),
                this.tempFile.getName()
            ).value()
        );
    }

    public void testValueOfUnknownVar() {
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
            new VarOfDotenv(
                "UNKNOWN",
                this.tempFile.getParent(),
                this.tempFile.getName()
            ).value();
        } catch (InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testValueWithWrongRegister() {
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
            new VarOfDotenv(
                "Login",
                this.tempFile.getParent(),
                this.tempFile.getName()
            ).value();
        } catch (InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testAssignmentOfVar() {
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
        new VarOfDotenv(
            "LOGIN",
            this.tempFile.getParent(),
            this.tempFile.getName()
        ).assignWith(new Text.Of("new-login"));
        try {
            assertEquals(
            """
                LOGIN=new-login
                """,
                Files.readString(this.tempFile.toPath())
            );
        } catch (final IOException e) {
            throw new AssertionError(e);
        }
    }

    public void testAssignmentWithBrokenValue() {
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
            new VarOfDotenv(
                "LOGIN",
                this.tempFile.getParent(),
                this.tempFile.getName()
            ).assignWith(() -> {
                throw new InvariantViolation("There is no text");
            });
            fail("InvariantViolation");
        } catch (final InvariantViolation ignored) {
            try {
                assertEquals(
                    """
                        LOGIN=login
                        """,
                    Files.readString(this.tempFile.toPath())
                );
            } catch (final IOException e) {
                throw new AssertionError(e);
            }
        }
    }

    public void testAssignmentWithBrokenName() {
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
            new VarOfDotenv(
                () -> {
                    throw new InvariantViolation("There is no text");
                },
                new Text.Of(this.tempFile.getParent()),
                new Text.Of(this.tempFile.getName())
            ).assignWith(new Text.Of("new-login"));
            fail("InvariantViolation");
        } catch (final InvariantViolation ignored) {
            try {
                assertEquals(
                    """
                        LOGIN=login
                        """,
                    Files.readString(this.tempFile.toPath())
                );
            } catch (final IOException e) {
                throw new AssertionError(e);
            }
        }
    }

    public void testValueFromUnknownFile() {
        try {
            new VarOfDotenv(
                "LOGIN",
                "path/to/non-existing-file",
                "i-am-not-real.txt"
            ).value();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testAssignmentFromUnknownFile() {
        try {
            new VarOfDotenv(
                "LOGIN",
                "path/to/non-existing-file",
                "i-am-not-real.txt"
            ).assignWith(new Text.Of("new-login"));
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }
}
