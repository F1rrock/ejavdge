package org.ejavdge.domain.problem;

import junit.framework.TestCase;
import org.ejavdge.contest.ProblemPage;
import org.ejavdge.dom.engine.JsoupWithSaxon;
import org.ejavdge.scalar.text.Text;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public final class DescriptionIT extends TestCase {
    private String html;

    @Override
    protected void setUp() throws IOException, URISyntaxException {
        final var url = getClass().getResource("/pages/problem.html");
        this.html = Files.readString(Path.of(Objects.requireNonNull(url).toURI()));
    }

    public void testContainsProblemText() {
        final var description = new Description(
            new JsoupWithSaxon(),
            new ProblemPage(new Text.Of(this.html))
        );
        final var result = description.content();
        assertTrue(result.contains("На стандартном потоке ввода задаются два целых числа"));
    }

    public void testContainsGoogleLink() {
        final var description = new Description(
            new JsoupWithSaxon(),
            new ProblemPage(new Text.Of(this.html))
        );
        final var result = description.content();
        assertTrue(result.contains("https://google.com"));
    }

    public void testContainsYoutubeLink() {
        final var description = new Description(
            new JsoupWithSaxon(),
            new ProblemPage(new Text.Of(this.html))
        );
        final var result = description.content();
        assertTrue(result.contains("https://youtube.com"));
    }

    public void testContainsAppleLink() {
        final var description = new Description(
            new JsoupWithSaxon(),
            new ProblemPage(new Text.Of(this.html))
        );
        final var result = description.content();
        assertTrue(result.contains("https://apple.com"));
    }

    public void testDoesNotContainTable() {
        final var description = new Description(
            new JsoupWithSaxon(),
            new ProblemPage(new Text.Of(this.html))
        );
        final var result = description.content();
        assertFalse(result.contains("Time limit"));
    }
}
