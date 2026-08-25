package org.ejavdge.dom.engine;

import junit.framework.TestCase;
import org.ejavdge.dom.path.DocPath;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

public final class JsoupWithSaxonTest extends TestCase {
    public void testSimpleXPath() {
        assertEquals(
            "Hello, world!",
            new JsoupWithSaxon().selectionOf(
                new Text.Of("<div id='main'>Hello, world!</div>"),
                new DocPath.Of("//*[local-name() = 'div'][@id='main']/text()")
            )
        );
    }

    public void testXPathWithStringJoin() {
        assertEquals(
            "https://example.com, https://test.com",
            new JsoupWithSaxon().selectionOf(
                new Text.Of(
                    """
                    <div><a href="https://example.com">Example</a></div>
                    <div><a href="https://test.com">Test</a></div>
                    """
                ),
                new DocPath.Of("string-join(//*[local-name() = 'a']/@href, ', ')")
            )
        );
    }

    public void testNoMatch() {
        assertEquals(
            "",
            new JsoupWithSaxon().selectionOf(
                new Text.Of("<div>Hello</div>"),
                new DocPath.Of("//*[local-name() = 'span']")
            )
        );
    }

    public void testInvalidXPath() {
        try {
            new JsoupWithSaxon().selectionOf(
                new Text.Of("<div>Hello</div>"),
                new DocPath.Of("string-join(//*, )")
            );
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testMalformedHtml() {
        assertEquals(
            "Hello",
            new JsoupWithSaxon().selectionOf(
                new Text.Of("<div id='main'><p>Hello</div>"),
                new DocPath.Of(
                    "//*[local-name()='div'][@id='main']/*[local-name()='p']/text()"
                )
            )
        );
    }

    public void testEmptyDocument() {
        assertEquals(
            "",
            new JsoupWithSaxon().selectionOf(
                new Text.Of(""),
                new DocPath.Of("//*")
            )
        );
    }

    public void testAttributeValue() {
        assertEquals(
            "https://example.com",
            new JsoupWithSaxon().selectionOf(
                new Text.Of("<a href='https://example.com'>click</a>"),
                new DocPath.Of("//*[local-name() = 'a']/@href")
            )
        );
    }
}
