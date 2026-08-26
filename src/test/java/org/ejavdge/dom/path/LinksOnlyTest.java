package org.ejavdge.dom.path;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

public final class LinksOnlyTest extends TestCase {
    public void testDefaultSeparator() {
        assertEquals(
            "string-join(//*[local-name() = 'a']//@href, '\n')",
            new LinksOnly(
                new DocPath.Of("//*")
            ).view()
        );
    }

    public void testCustomSeparator() {
        assertEquals(
            "string-join(//*[local-name() = 'a']//@href, ', ')",
            new LinksOnly(
                new Text.Of(", "),
                new DocPath.Of("//*")
            ).view()
        );
    }

    public void testBrokenPath() {
        try {
            new LinksOnly(
                () -> {
                    throw new InvariantViolation("path error");
                }
            ).view();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testBrokenSeparator() {
        try {
            new LinksOnly(
                () -> {
                    throw new InvariantViolation("separator error");
                },
                new DocPath.Of("//*")
            ).view();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }
}
