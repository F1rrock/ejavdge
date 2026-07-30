package org.ejavdge.web.spec.header;

import junit.framework.TestCase;
import org.ejavdge.scalar.text.Text;

import java.nio.charset.StandardCharsets;

public final class HeaderTest extends TestCase {
    public void testHeader() {
        assertEquals(
            "Content-Type: text/html\r\n",
            new String(
                new Header(
                    new Text.Of("Content-Type"),
                    new Text.Of("text/html")
                ).content(),
                StandardCharsets.US_ASCII
            )
        );
    }
}