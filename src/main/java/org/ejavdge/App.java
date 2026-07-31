package org.ejavdge;

import org.ejavdge.scalar.num.NumOfText;
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

@SuppressWarnings("java:S106")
public final class App
{
    public static void main(final String[] args)
    {
        final var loc = new Location(
            new Text.Of(args[0]),
            new Text.Of(args[1]),
            new NumOfText(args[2])
        );
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
                                    new Credentials(
                                        new Text.Of(args[3]),
                                        new Text.Of(args[4]),
                                        new NumOfText(args[5])
                                    )
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
