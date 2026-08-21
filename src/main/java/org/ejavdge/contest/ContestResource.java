package org.ejavdge.contest;

import org.ejavdge.auth.Session;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.BindOfBytes;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.bytes.BytesAbout;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.web.context.*;
import org.ejavdge.web.driver.WebDriver;
import org.ejavdge.web.media.Cookies;
import org.ejavdge.web.resource.WebResource;
import org.ejavdge.web.spec.Request;
import org.ejavdge.web.spec.header.Header;
import org.ejavdge.web.spec.header.WithHeaders;
import org.ejavdge.web.spec.method.Get;

public final class ContestResource implements Bytes {
    private final WebDriver driver;
    private final Location location;
    private final Session session;

    public ContestResource(final ContestResource r, final Context q) {
        this(
            r.driver,
            new Location(r.location, q),
            r.session
        );
    }

    public ContestResource(final WebDriver d, final Location l, final Session s) {
        this.driver = d;
        this.location = l;
        this.session = s;
    }

    @Override
    public byte[] content() throws InvariantViolation {
        return new BytesAbout(
            "contest resource",
            new BindOfBytes(
                this.session,
                s -> new WebResource(
                    this.driver,
                    this.location,
                    new Request(
                        new WithHeaders(
                            new Header(
                                new Text.Of("Cookie"),
                                new Cookies.ImprintOf(
                                    new ContextOfEjsid(s)
                                )
                            ),
                            new Get(
                                new Location(
                                    this.location,
                                    new ContextOfSid(s)
                                )
                            )
                        )
                    )
                )
            )
        ).content();
    }
}
