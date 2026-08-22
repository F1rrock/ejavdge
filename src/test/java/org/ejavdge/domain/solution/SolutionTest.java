package org.ejavdge.domain.solution;

import junit.framework.TestCase;
import org.ejavdge.auth.Session;
import org.ejavdge.contest.ContestForm;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.file.ByteFile;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.web.context.Context;
import org.ejavdge.web.context.ContextOfSolution;
import org.ejavdge.web.context.Location;
import org.ejavdge.web.media.Media;

import java.nio.charset.StandardCharsets;

public final class SolutionTest extends TestCase {
    public void testValidSolution() {
        try {
            new Solution(
                new ContestForm(
                    new FakeDriver(
                        """
                           POST /ejudge HTTP/1.1\r
                           Host: localhost:90\r
                           Cookie: EJSID=756b423a0a6fe6a7\r
                           Content-Type: multipart/form-data; boundary={BOUNDARY}\r
                           Content-Length: 728\r
                           \r
                           --{BOUNDARY}\r
                           Content-Disposition: form-data; name="SID"\r
                           \r
                           1684bb4a0f94302c\r
                           --{BOUNDARY}\r
                           Content-Disposition: form-data; name="action_40"\r
                           \r
                           Send!\r
                           --{BOUNDARY}\r
                           Content-Disposition: form-data; name="prob_id"\r
                           \r
                           1\r
                           --{BOUNDARY}\r
                           Content-Disposition: form-data; name="lang_id"\r
                           \r
                           82\r
                           --{BOUNDARY}\r
                           Content-Disposition: form-data; name="file"; filename="Sample.java"\r
                           Content-Type: application/octet-stream\r
                           \r
                           Hello, World!\r
                           --{BOUNDARY}--\r
                           """
                    ),
                    new Location(
                        new Text.Of("/ejudge"),
                        new Text.Of("localhost"),
                        new Num.Of(90)
                    ),
                    new Session(
                        new Bytes.Of(
                            """
                            HTTP/1.1 302 FOUND\r
                            Set-Cookie: EJSID=756b423a0a6fe6a7;\r
                            Location: http://0.0.0.0:90/ejudge?SID=1684bb4a0f94302c&action=2&lt=1\r
                            Content-Length: 2\r
                            \r
                            OK\r
                            """.getBytes(StandardCharsets.UTF_8)
                        )
                    )
                ),
                new ContextOfSolution(
                    new Context() {
                        @Override
                        public <T> T imprint(final Media<T> m) throws InvariantViolation {
                            return m
                                .with(new Text.Of("prob_id"), new Text.Of("1"))
                                .with(new Text.Of("lang_id"), new Text.Of("82"))
                                .content();
                        }
                    }
                ),
                new ByteFile.Of(
                    new Text.Of("Sample.java"),
                    new Bytes.Of("Hello, World!".getBytes(StandardCharsets.UTF_8))
                )
            ).send();
        } catch (final InvariantViolation e) {
            fail(e.getMessage());
        }
    }

    public void testWithInvalidSession() {
        try {
            new Solution(
                new ContestForm(
                    new FakeDriver(
                        """
                           POST /ejudge HTTP/1.1\r
                           Host: localhost:90\r
                           Cookie: EJSID=756b423a0a6fe6a7\r
                           Content-Type: multipart/form-data; boundary={BOUNDARY}\r
                           Content-Length: 728\r
                           \r
                           --{BOUNDARY}\r
                           Content-Disposition: form-data; name="SID"\r
                           \r
                           1684bb4a0f94302c\r
                           --{BOUNDARY}\r
                           Content-Disposition: form-data; name="action_40"\r
                           \r
                           Send!\r
                           --{BOUNDARY}\r
                           Content-Disposition: form-data; name="prob_id"\r
                           \r
                           1\r
                           --{BOUNDARY}\r
                           Content-Disposition: form-data; name="lang_id"\r
                           \r
                           82\r
                           --{BOUNDARY}\r
                           Content-Disposition: form-data; name="file"; filename="Sample.java"\r
                           Content-Type: application/octet-stream\r
                           \r
                           Hello, World!\r
                           --{BOUNDARY}--\r
                           """
                    ),
                    new Location(
                        new Text.Of("/ejudge"),
                        new Text.Of("localhost"),
                        new Num.Of(90)
                    ),
                    new Session(
                        new Bytes.Of(
                            """
                            HTTP/1.1 200 OK\r
                            Content-Length: 19\r
                            \r
                            Invalid credentials\r
                            """.getBytes(StandardCharsets.UTF_8)
                        )
                    )
                ),
                new ContextOfSolution(
                    new Context() {
                        @Override
                        public <T> T imprint(final Media<T> m) throws InvariantViolation {
                            return m
                                .with(new Text.Of("prob_id"), new Text.Of("1"))
                                .with(new Text.Of("lang_id"), new Text.Of("82"))
                                .content();
                        }
                    }
                ),
                new ByteFile.Of(
                    new Text.Of("Sample.java"),
                    new Bytes.Of("Hello, World!".getBytes(StandardCharsets.UTF_8))
                )
            ).send();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }
}
