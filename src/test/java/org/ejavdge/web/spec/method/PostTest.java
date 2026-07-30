package org.ejavdge.web.spec.method;

import junit.framework.TestCase;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.scalar.text.Text;

import java.nio.charset.StandardCharsets;

public final class PostTest extends TestCase {
    public void testRequest() {
        assertEquals(
            """
            POST /ejudge HTTP/1.1\r
            Host: example.com:80\r
            """,
            new String(
                new Post(
                    new Text.Of("/ejudge"),
                    new Text.Of("example.com"),
                    new Num.Of(80)
                ).bytes(),
                StandardCharsets.US_ASCII
            )
        );
    }
}
