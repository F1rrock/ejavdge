package org.ejavdge.scalar.text;

import junit.framework.TestCase;

import static org.junit.Assert.assertNotEquals;

public final class ContentBasedTest extends TestCase {
    public void testEqualContents() {
        assertEquals(
            new ContentBased(new Text.Of("HelloWorld")),
            new ContentBased(new Text.Of("HelloWorld"))
        );
    }

    public void testNonEqualContents() {
        assertNotEquals(
            new ContentBased(new Text.Of("HelloWorld")),
            new ContentBased(new Text.Of("HelloWorld!"))
        );
    }
}
