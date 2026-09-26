package org.ejavdge.app;

import junit.framework.TestCase;
import org.ejavdge.auth.Session;
import org.ejavdge.contest.ContestResource;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.file.ByteFile;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.web.context.Location;

import java.nio.charset.StandardCharsets;

public final class AttachmentsDownloadIT extends TestCase {
    public void testFileWithoutProblemMarker() {
        try {
            new AttachmentsDownload(
                new ContestResource(
                    (loc, req) -> req.bytes(),
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
                            Location: http://localhost:90/ejudge?SID=1684bb4a0f94302c&action=2&lt=1\r
                            Content-Length: 2\r
                            \r
                            OK\r
                            """.getBytes(StandardCharsets.UTF_8)
                        )
                    )
                ),
                new ByteFile.Of(
                    new Text.Of("without marker"),
                    new Bytes.Of(
                        """
                        public final class WithoutMarker {
                        }
                        """.getBytes(StandardCharsets.UTF_8)
                    )
                ),
                new Text.Of("./")
            ).run();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testWithInvalidSession() {
        try {
            new AttachmentsDownload(
                new ContestResource(
                    (loc, req) -> req.bytes(),
                    new Location(
                        new Text.Of("/ejudge"),
                        new Text.Of("localhost"),
                        new Num.Of(90)
                    ),
                    new Session(
                        new Bytes.Of(
                            """
                            HTTP/1.1 200 OK\r
                            Location: http://localhost:90/ejudge&action=2&lt=1\r
                            Content-Length: 15\r
                            \r
                            Invalid session\r
                            """.getBytes(StandardCharsets.UTF_8)
                        )
                    )
                ),
                new ByteFile.Of(
                    new Text.Of("without marker"),
                    new Bytes.Of(
                        """
                        public final class WithoutMarker {
                        }
                        """.getBytes(StandardCharsets.UTF_8)
                    )
                ),
                new Text.Of("./")
            ).run();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }
}
