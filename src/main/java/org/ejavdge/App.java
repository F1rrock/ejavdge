package org.ejavdge;

import org.ejavdge.scalar.text.Text;
import org.ejavdge.web.context.Credentials;
import org.ejavdge.web.context.Location;
import org.ejavdge.web.context.WithEntry;
import org.ejavdge.web.driver.jdk.socket.JdkSocket;
import org.ejavdge.web.media.Form;
import org.ejavdge.web.resource.WebResource;
import org.ejavdge.web.spec.Request;
import org.ejavdge.web.spec.body.WithBody;
import org.ejavdge.web.spec.header.Header;
import org.ejavdge.web.spec.header.WithHeaders;
import org.ejavdge.web.spec.method.Post;

import java.nio.charset.StandardCharsets;

public final class App
{
    public static void main(final String[] args)
    {
        final var loc = new Location("/ejudge", "0.0.0.0", 90);
        System.out.println(
            new String(
                new WebResource(
                    new JdkSocket(),
                    loc,
                    new Request(
                        new WithBody(
                            new Form.ImprintOf(
                                new WithEntry(
                                    new Text.Of("action_2"),
                                    new Text.Of("Log in"),
                                    new Credentials("vader", "ejudge", 1)
                                )
                            ),
                            new WithHeaders(
                                new Header(
                                    new Text.Of("Content-Type"),
                                    new Text.Of("application/x-www-form-urlencoded")
                                ),
                                new Post(loc)
                            )
                        )
                    )
                ).content(),
                StandardCharsets.US_ASCII
            )
        );
    }
}
