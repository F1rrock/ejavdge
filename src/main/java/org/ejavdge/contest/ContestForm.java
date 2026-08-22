package org.ejavdge.contest;

import org.ejavdge.auth.Session;
import org.ejavdge.effect.Envelope;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.items.Items;
import org.ejavdge.items.Joint;
import org.ejavdge.scalar.bytes.BindOfBytes;
import org.ejavdge.scalar.bytes.BytesAbout;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.web.context.ContextOfEjsid;
import org.ejavdge.web.context.ContextOfSid;
import org.ejavdge.web.context.Location;
import org.ejavdge.web.driver.WebDriver;
import org.ejavdge.web.media.Cookies;
import org.ejavdge.web.resource.HasStatus;
import org.ejavdge.web.resource.WebResource;
import org.ejavdge.web.spec.Request;
import org.ejavdge.web.spec.body.multipart.Multipart;
import org.ejavdge.web.spec.body.multipart.Part;
import org.ejavdge.web.spec.body.multipart.TextParts;
import org.ejavdge.web.spec.header.Header;
import org.ejavdge.web.spec.header.WithHeaders;
import org.ejavdge.web.spec.method.Post;

public final class ContestForm implements Envelope {
    private final WebDriver driver;
    private final Location location;
    private final Session session;
    private final Items<Part> fields;

    public ContestForm(final ContestForm cf, final Items<Part> ps) {
        this.driver = cf.driver;
        this.location = cf.location;
        this.session = cf.session;
        this.fields = new Joint<>(cf.fields, ps);
    }

    public ContestForm(final WebDriver d, final Location l, final Session s) {
        this.driver = d;
        this.location = l;
        this.session = s;
        this.fields = new Items.Of<>();
    }

    @Override
    public void send() throws InvariantViolation {
        new BytesAbout(
            "contest form",
            new BindOfBytes(
                this.session,
                s -> new HasStatus(
                    new Num.Of(302),
                    new WebResource(
                        this.driver,
                        this.location,
                        new Request(
                            new Multipart(
                                new Joint<>(
                                    new TextParts.ImprintOf(
                                        new ContextOfSid(s)
                                    ),
                                    this.fields
                                ),
                                new WithHeaders(
                                    new Header(
                                        new Text.Of("Cookie"),
                                        new Cookies.ImprintOf(
                                            new ContextOfEjsid(s)
                                        )
                                    ),
                                    new Post(this.location)
                                )
                            )
                        )
                    )
                )
            )
        ).content();
    }
}
