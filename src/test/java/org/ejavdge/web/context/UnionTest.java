package org.ejavdge.web.context;

import junit.framework.TestCase;
import org.ejavdge.web.media.FakeMedia;

public final class UnionTest extends TestCase {
    public void testDouble() {
        assertEquals(
            "name 1:value 1:name 2:value 2:name 1:value 1:name 2:value 2:",
            new Union(
                new FakeContext(),
                new FakeContext()
            ).imprint(new FakeMedia())
        );
    }

    public void testUnionWithEmpty() {
        assertEquals(
            "name 1:value 1:name 2:value 2:",
            new Union(
                new FakeContext(),
                new NoContext()
            ).imprint(new FakeMedia())
        );
    }

    public void testUnionCompose() {
        assertEquals(
            "name 1:value 1:name 2:value 2:name 1:value 1:name 2:value 2:",
            new Union(
                new FakeContext(),
                new Union(
                    new FakeContext(),
                    new NoContext()
                )
            ).imprint(new FakeMedia())
        );
    }
}
