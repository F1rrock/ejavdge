package org.ejavdge.web.media;

import junit.framework.TestCase;
import org.ejavdge.items.Items;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.web.context.FakeContext;

public final class WhiteListTest extends TestCase {
    public void testAllowedNames() {
        assertEquals(
            "name 1:value 1:",
            new FakeContext().imprint(
                new WhiteList<>(
                    new Items.Of<>(
                        new Text.Of("name 1")
                    ),
                    new FakeMedia()
                )
            )
        );
    }

    public void testComposition() {
        assertEquals(
            "",
            new FakeContext().imprint(
                new WhiteList<>(
                    new Items.Of<>(
                        new Text.Of("name 1")
                    ),
                    new WhiteList<>(
                        new Items.Of<>(
                            new Text.Of("name 2")
                        ),
                        new FakeMedia()
                    )
                )
            )
        );
    }
}
