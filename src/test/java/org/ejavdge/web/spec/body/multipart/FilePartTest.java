package org.ejavdge.web.spec.body.multipart;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.file.ByteFile;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.text.Empty;
import org.ejavdge.scalar.text.Text;

import java.nio.charset.StandardCharsets;

public final class FilePartTest extends TestCase {
    public void testHelloWorld() {
        assertEquals(
            """
            Content-Disposition: form-data; name="file"; filename="hello.py"\r
            Content-Type: application/octet-stream\r
            \r
            Hello, world!""",
            new String(
                new FilePart(
                    new Text.Of("hello.py"),
                    new Bytes.Of("Hello, world!".getBytes(StandardCharsets.UTF_8))
                ).content(),
                StandardCharsets.UTF_8
            )
        );
    }

    public void testWithoutName() {
        try {
            new FilePart(
                new Empty(),
                new Bytes.Of("bla bla".getBytes(StandardCharsets.UTF_8))
            ).content();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testWithoutBody() {
        assertEquals(
            """
            Content-Disposition: form-data; name="file"; filename="Empty.java"\r
            Content-Type: application/octet-stream\r
            \r
            """,
            new String(
                new FilePart(
                    new Text.Of("Empty.java"),
                    new Bytes.Of(new byte[0])
                ).content(),
                StandardCharsets.UTF_8
            )
        );
    }

    public void testWithFile() {
        assertEquals(
            """
            Content-Disposition: form-data; name="file"; filename="hello.py"\r
            Content-Type: application/octet-stream\r
            \r
            Hello, world!""",
            new String(
                new FilePart(
                    new ByteFile.Of(
                        new Text.Of("hello.py"),
                        new Bytes.Of("Hello, world!".getBytes(StandardCharsets.UTF_8))
                    )
                ).content(),
                StandardCharsets.UTF_8
            )
        );
    }
}
