package org.ejavdge.domain.problem;

import junit.framework.TestCase;
import org.ejavdge.contest.ProblemPage;
import org.ejavdge.dom.engine.JsoupWithSaxon;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.items.Items;
import org.ejavdge.scalar.text.Text;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

public final class ProbAttachmentsIT extends TestCase {
    private String problem;

    @Override
    public void setUp() throws Exception {
        this.problem = Files.readString(
            new File(
                "src/test/resources/pages/problem.html"
            ).toPath()
        );
    }

    public void testForNbsp() {
        for (final var link : this.links()) {
            assertFalse(link.contains("\u00A0"));
        }
    }

    public void testNoEmptyLines() {
        for (final var link : this.links()) {
            assertFalse(link.isEmpty());
        }
    }

    public void testNoWhitespaceOnlyLines() {
        for (final var link : this.links()) {
            assertFalse(
                "Whitespace-only line: [" + link + "]",
                link.isBlank()
            );
        }
    }

    public void testLinksAreTrimmed() {
        for (final var link : this.links()) {
            assertEquals(
                "Untrimmed link: [" + link + "]",
                link,
                link.trim()
            );
        }
    }

    public void testGoogleLink() {
        assertFalse(
            this.links().contains("https://google.com")
        );
    }

    public void testYoutubeLink() {
        assertFalse(
            this.links().contains("https://youtube.com")
        );
    }

    public void testAppleLink() {
        assertFalse(
            this.links().contains("https://apple.com")
        );
    }

    public void testNoInnerText() {
        final var joined = String.join("\n", this.links());
        assertFalse(joined.contains("На стандартном потоке"));
    }

    public void testOnlyLinks() {
        for (final var link : this.links()) {
            assertTrue(
                "Not a link: [" + link + "]",
                link.startsWith("http://") || link.startsWith("https://")
            );
        }
    }

    public void testFullListOfAttachments() {
        assertEquals(
            List.of("https://localhost:8000/sample.txt"),
            this.links()
        );
    }

    public void testWrongPage() {
        assertTrue(
            new ProbAttachments(
                new JsoupWithSaxon(),
                new ProblemPage(
                    new Text.Of(
                        """
                        <html>
                            <body>
                                <p>404 Not found</p>
                            </body>
                        </html>
                        """
                    )
                )
            ).contents().isEmpty()
        );
    }

    public void testBrokenPage() {
        try {
            new ProbAttachments(
                new JsoupWithSaxon(),
                new ProblemPage(
                    () -> {
                        throw new InvariantViolation("There is no text.");
                    }
                )
            ).contents();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testBrokenEngine() {
        try {
            new ProbAttachments(
                (xml, path) -> {
                    throw new InvariantViolation("There is no engine.");
                },
                new ProblemPage(
                    new Text.Of(this.problem)
                )
            ).contents();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testFromItems() throws InvariantViolation {
        final var ts = new Items.Of<Text>(
            new Text.Of("https://a.com"),
            new Text.Of("https://b.com")
        );
        assertEquals(
            ts.contents(),
            new ProbAttachments(ts).contents()
        );
    }

    private List<String> links() {
        return new ProbAttachments(
            new JsoupWithSaxon(),
            new ProblemPage(
                new Text.Of(this.problem)
            )
        ).contents()
            .stream()
            .map(Text::content)
            .toList();
    }
}
