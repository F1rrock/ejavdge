package org.ejavdge.domain.problem;

import junit.framework.TestCase;
import org.ejavdge.contest.MainPage;
import org.ejavdge.dom.engine.JsoupWithSaxon;
import org.ejavdge.scalar.text.Text;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public final class SolvedIT extends TestCase {
    private String html;

    @Override
    protected void setUp() throws IOException, URISyntaxException {
        final var url = getClass().getResource("/pages/main.html");
        this.html = Files.readString(Path.of(Objects.requireNonNull(url).toURI()));
    }

    public void testContainsProblemA() {
        final var solved = new Solved(
            new JsoupWithSaxon(),
            new MainPage(new Text.Of(this.html))
        );
        final var result = solved.content();
        assertTrue(result.contains("A"));
    }

    public void testDoesNotContainProblemB() {
        final var solved = new Solved(
            new JsoupWithSaxon(),
            new MainPage(new Text.Of(this.html))
        );
        final var result = solved.content();
        assertFalse(result.contains("B"));
    }

    public void testDoesNotContainProblemWithLinks() {
        final var solved = new Solved(
            new JsoupWithSaxon(),
            new MainPage(new Text.Of(this.html))
        );
        final var result = solved.content();
        assertFalse(result.contains("WithLinks"));
    }

    public void testDoesNotContainInfo() {
        final var solved = new Solved(
            new JsoupWithSaxon(),
            new MainPage(new Text.Of(this.html))
        );
        final var result = solved.content();
        assertFalse(result.contains("Info"));
    }

    public void testDoesNotContainSummary() {
        final var solved = new Solved(
            new JsoupWithSaxon(),
            new MainPage(new Text.Of(this.html))
        );
        final var result = solved.content();
        assertFalse(result.contains("Summary"));
    }

    public void testDoesNotContainSubmissions() {
        final var solved = new Solved(
            new JsoupWithSaxon(),
            new MainPage(new Text.Of(this.html))
        );
        final var result = solved.content();
        assertFalse(result.contains("Submissions"));
    }
}
