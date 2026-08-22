package org.ejavdge.web.context;

import junit.framework.TestCase;
import org.ejavdge.web.media.FakeMedia;

public final class ContextOfSolutionTest extends TestCase {
    public void testImprint() {
        assertEquals(
            "prob_id:1:lang_id:90:",
            new ContextOfSolution(
                new ProbId(1),
                new LangId(90)
            ).imprint(new FakeMedia())
        );
    }
}
