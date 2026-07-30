package org.ejavdge.web.context;

import junit.framework.TestCase;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.web.media.FakeMedia;

public final class WithEntryTest extends TestCase {
    public void testAdditionalEntry() {
        assertEquals(
            "name 0:value 0:name 1:value 1:name 2:value 2:",
            new WithEntry(
                new Text.Of("name 0"),
                new Text.Of("value 0"),
                new FakeContext()
            ).imprint(new FakeMedia())
        );
    }
}
