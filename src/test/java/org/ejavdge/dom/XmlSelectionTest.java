package org.ejavdge.dom;

import junit.framework.TestCase;
import org.ejavdge.dom.path.DocPath;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

public final class XmlSelectionTest extends TestCase {
    public void testDelegation() {
        assertEquals(
            "Hello",
            new XmlSelection(
                (t, p) -> {
                    if (t.content().equals("<div>Hello</div>") && p.view().equals("//*/text()")) {
                        return "Hello";
                    } else {
                        throw new InvariantViolation("unsupported");
                    }
                },
                new Text.Of("<div>Hello</div>"),
                new DocPath.Of("//*/text()")
            ).content()
        );
    }

    public void testError() {
        try {
            new XmlSelection(
                (t, p) -> {
                    throw new InvariantViolation("unsupported");
                },
                new Text.Of("<div>Hello</div>"),
                new DocPath.Of("//*/text()")
            ).content();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testEmptyResult() {
        assertEquals(
            "",
            new XmlSelection(
                (t, p) -> "",
                new Text.Of("<div>Hello</div>"),
                new DocPath.Of("//*/text()")
            ).content()
        );
    }
}
