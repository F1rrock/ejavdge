package org.ejavdge.web.media;

import junit.framework.TestCase;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.web.context.FakeContext;

import java.util.List;

public final class GistTest extends TestCase {
    public void testNamesAreIgnored() {
        assertEquals(
            List.of("value 1", "value 2"),
            new Gist.ImprintOf(new FakeContext())
                .contents()
                .stream()
                .map(Text::content)
                .toList()
        );
    }
}
