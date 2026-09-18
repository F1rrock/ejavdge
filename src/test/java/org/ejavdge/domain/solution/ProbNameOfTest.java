package org.ejavdge.domain.solution;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.file.ByteFile;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.text.Text;

import java.nio.charset.StandardCharsets;

public final class ProbNameOfTest extends TestCase {
    public void testWithoutSpaces() {
        assertEquals(
            "A",
            new ProbNameOf(
                new ByteFile.Of(
                    new Text.Of("ProbA.java"),
                    new Bytes.Of(
                        """
                        //problem:A
                        """.getBytes(StandardCharsets.UTF_8)
                    )
                )
            ).content()
        );
    }

    public void testWithSingleSpaces() {
        assertEquals(
            "A",
            new ProbNameOf(
                new ByteFile.Of(
                    new Text.Of("ProbA.java"),
                    new Bytes.Of(
                        """
                        // problem: A
                        """.getBytes(StandardCharsets.UTF_8)
                    )
                )
            ).content()
        );
    }

    public void testWithTabs() {
        assertEquals(
            "A",
            new ProbNameOf(
                new ByteFile.Of(
                    new Text.Of("ProbA.java"),
                    new Bytes.Of(
                        """
                        //  problem:    A
                        """.getBytes(StandardCharsets.UTF_8)
                    )
                )
            ).content()
        );
    }

    public void testWithSeveralOccurrences() {
        assertEquals(
            "A",
            new ProbNameOf(
                new ByteFile.Of(
                    new Text.Of("ProbA.java"),
                    new Bytes.Of(
                        """
                        // problem: A
                        // problem: B
                        """.getBytes(StandardCharsets.UTF_8)
                    )
                )
            ).content()
        );
    }

    public void testWithoutMarker() {
        try {
            new ProbNameOf(
                new ByteFile.Of(
                    new Text.Of("ProbA.java"),
                    new Bytes.Of(
                        """
                        // language: 15
                        """.getBytes(StandardCharsets.UTF_8)
                    )
                )
            ).content();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testWithNewLines() {
        try {
            new ProbNameOf(
                new ByteFile.Of(
                    new Text.Of("ProbA.java"),
                    new Bytes.Of(
                        """
                        //
                        problem:
                        15
                        """.getBytes(StandardCharsets.UTF_8)
                    )
                )
            ).content();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }
}
