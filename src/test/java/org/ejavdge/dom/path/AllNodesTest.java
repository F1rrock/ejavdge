package org.ejavdge.dom.path;

import junit.framework.TestCase;

public final class AllNodesTest extends TestCase {
    public void testXPath() {
        assertEquals("//*", new AllNodes().view());
    }
}
