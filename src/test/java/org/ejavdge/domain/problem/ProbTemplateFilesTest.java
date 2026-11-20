package org.ejavdge.domain.problem;

import junit.framework.TestCase;
import org.ejavdge.auth.Session;
import org.ejavdge.contest.ContestResource;
import org.ejavdge.dom.engine.JsoupWithSaxon;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.scalar.text.Utf8Text;
import org.ejavdge.web.context.Location;
import org.ejavdge.web.driver.WebDriver;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public final class ProbTemplateFilesTest extends TestCase {
    private static final String MAIN = """
        <html>
            <body>
                <table>
                    <tr id="probNavTopList">
                        <td>
                            <ul class="nTopNavList">
                                <li><a class="tab" href="?prob_id=7">A</a></li>
                                <li><a class="tab" href="?prob_id=3">B</a></li>
                            </ul>
                        </td>
                    </tr>
                </table>
            </body>
        </html>
        """;

    public void testHeaderAndFooterByProblemName() {
        final var requests = new ArrayList<String>();
        final var contest = this.resource(
            (loc, req) -> {
                final var target = new String(req.bytes(), StandardCharsets.UTF_8).split(" ", 3)[1];
                requests.add(target);
                if (target.contains("amp%3Baction=2&")) {
                    return MAIN.getBytes(StandardCharsets.UTF_8);
                }
                if (target.contains("file=HeaderOfB.java&")) {
                    return this.attachment("class Main {");
                }
                if (target.contains("file=FooterOfB.java&")) {
                    return this.attachment("}");
                }
                throw new AssertionError("Unexpected template request");
            }
        );
        final var engine = new JsoupWithSaxon();
        assertEquals("class Main {", new Utf8Text(new ProbHeader(new Text.Of("B"), contest, engine)).content());
        assertEquals("}", new Utf8Text(new ProbFooter(new Text.Of("B"), contest, engine)).content());
        assertEquals(
            List.of(
                "/ejudge?amp%3Baction=2&amp%3Blt=1&SID=abc",
                "/ejudge?action=194&file=HeaderOfB.java&prob_id=3&SID=abc",
                "/ejudge?amp%3Baction=2&amp%3Blt=1&SID=abc",
                "/ejudge?action=194&file=FooterOfB.java&prob_id=3&SID=abc"
            ),
            requests
        );
    }

    private byte[] attachment(final String source) {
        return """
            HTTP/1.1 200 OK\r
            Content-Disposition: attachment\r
            \r
            %s""".formatted(source).getBytes(StandardCharsets.UTF_8);
    }

    private ContestResource resource(final WebDriver driver) {
        return new ContestResource(
            driver,
            new Location(new Text.Of("/ejudge"), new Text.Of("localhost"), new Num.Of(90)),
            new Session(
                new Bytes.Of(
                    """
                    HTTP/1.1 302 Found\r
                    Set-Cookie: EJSID=def;\r
                    Location: http://localhost:90/ejudge?SID=abc&action=2&lt=1\r
                    \r
                    """.getBytes(StandardCharsets.UTF_8)
                )
            )
        );
    }
}
