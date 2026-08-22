package org.ejavdge.web.spec.body.multipart;

import junit.framework.TestCase;
import org.ejavdge.items.Items;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.web.spec.HttpSpec;

import java.nio.charset.StandardCharsets;

public final class MultipartTest extends TestCase {
    public void testSinglePart() {
        assertEquals(
            """
            POST /ejudge/ HTTP/1.1\r
            Host: 0.0.0.0:90\r
            Cookie: EJSID=b3093b8047604d1e\r
            Content-Type: multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW\r
            Content-Length: 146\r
            \r
            ------WebKitFormBoundary7MA4YWxkTrZu0gW\r
            Content-Disposition: form-data; name="SID"\r
            \r
            0df2b2e12d05ac44\r
            ------WebKitFormBoundary7MA4YWxkTrZu0gW--""",
            new String(
                new Multipart(
                    new Items.Of<>(
                        new Part.Of(
                            new Bytes.Of(
                                """
                                Content-Disposition: form-data; name="SID"\r
                                \r
                                0df2b2e12d05ac44""".getBytes(StandardCharsets.UTF_8)
                            )
                        )
                    ),
                    new Boundary(
                        new Bytes.Of(
                            "----WebKitFormBoundary7MA4YWxkTrZu0gW"
                                .getBytes(StandardCharsets.UTF_8)
                        )
                    ),
                    new HttpSpec.Of(
                        new Bytes.Of(
                            """
                            POST /ejudge/ HTTP/1.1\r
                            Host: 0.0.0.0:90\r
                            Cookie: EJSID=b3093b8047604d1e\r
                            """.getBytes(StandardCharsets.UTF_8)
                        )
                    )
                ).bytes(),
                StandardCharsets.UTF_8
            )
        );
    }

    public void testMultipleParts() {
        assertEquals(
            """
            POST /ejudge/ HTTP/1.1\r
            Host: 0.0.0.0:90\r
            Cookie: EJSID=b3093b8047604d1e\r
            Content-Type: multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW\r
            Content-Length: 240\r
            \r
            ------WebKitFormBoundary7MA4YWxkTrZu0gW\r
            Content-Disposition: form-data; name="SID"\r
            \r
            0df2b2e12d05ac44\r
            ------WebKitFormBoundary7MA4YWxkTrZu0gW\r
            Content-Disposition: form-data; name="prob_id"\r
            \r
            1\r
            ------WebKitFormBoundary7MA4YWxkTrZu0gW--""",
            new String(
                new Multipart(
                    new Items.Of<>(
                        new Part.Of(
                            new Bytes.Of(
                                """
                                Content-Disposition: form-data; name="SID"\r
                                \r
                                0df2b2e12d05ac44""".getBytes(StandardCharsets.UTF_8)
                            )
                        ),
                        new Part.Of(
                            new Bytes.Of(
                                """
                                Content-Disposition: form-data; name="prob_id"\r
                                \r
                                1""".getBytes(StandardCharsets.UTF_8)
                            )
                        )
                    ),
                    new Boundary(
                        new Bytes.Of(
                            "----WebKitFormBoundary7MA4YWxkTrZu0gW"
                                .getBytes(StandardCharsets.UTF_8)
                        )
                    ),
                    new HttpSpec.Of(
                        new Bytes.Of(
                            """
                            POST /ejudge/ HTTP/1.1\r
                            Host: 0.0.0.0:90\r
                            Cookie: EJSID=b3093b8047604d1e\r
                            """.getBytes(StandardCharsets.UTF_8)
                        )
                    )
                ).bytes(),
                StandardCharsets.UTF_8
            )
        );
    }

    public void testWithoutParts() {
        assertEquals(
            """
            POST /ejudge/ HTTP/1.1\r
            Host: 0.0.0.0:90\r
            Cookie: EJSID=b3093b8047604d1e\r
            Content-Type: multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW\r
            Content-Length: 41\r
            \r
            ------WebKitFormBoundary7MA4YWxkTrZu0gW--""",
            new String(
                new Multipart(
                    new Items.Of<>(),
                    new Boundary(
                        new Bytes.Of(
                            "----WebKitFormBoundary7MA4YWxkTrZu0gW"
                                .getBytes(StandardCharsets.UTF_8)
                        )
                    ),
                    new HttpSpec.Of(
                        new Bytes.Of(
                            """
                            POST /ejudge/ HTTP/1.1\r
                            Host: 0.0.0.0:90\r
                            Cookie: EJSID=b3093b8047604d1e\r
                            """.getBytes(StandardCharsets.UTF_8)
                        )
                    )
                ).bytes(),
                StandardCharsets.UTF_8
            )
        );
    }
}
