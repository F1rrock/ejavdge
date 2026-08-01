package org.ejavdge.auth;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.web.context.Credentials;
import org.ejavdge.web.context.Location;
import org.ejavdge.web.context.WithEntry;
import org.ejavdge.web.driver.WebDriver;
import org.ejavdge.web.media.Form;
import org.ejavdge.web.resource.WebResource;
import org.ejavdge.web.spec.Request;
import org.ejavdge.web.spec.body.WithBody;
import org.ejavdge.web.spec.header.Header;
import org.ejavdge.web.spec.header.WithHeaders;
import org.ejavdge.web.spec.method.Post;

public final class LoginReply implements Bytes {
    private final Bytes origin;

    public LoginReply(final WebDriver d, final Location l, final Credentials c) {
        this(
            new WebResource(
                d, l,
                new Request(
                    new WithBody(
                        new Form.ImprintOf(
                            new WithEntry(
                                new Text.Of("action_2"),
                                new Text.Of("Log in"),
                                c
                            )
                        ),
                        new WithHeaders(
                            new Header(
                                new Text.Of("Content-Type"),
                                new Text.Of("application/x-www-form-urlencoded")
                            ),
                            new Post(l)
                        )
                    )
                )
            )
        );
    }

    public LoginReply(final Bytes origin) {
        this.origin = origin;
    }

    @Override
    public byte[] content() throws InvariantViolation {
        return this.origin.content();
    }
}
