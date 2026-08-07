package org.ejavdge.page;

import org.ejavdge.auth.Session;
import org.ejavdge.domain.tokens.Ejsid;
import org.ejavdge.domain.tokens.Sid;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.scalar.text.Utf8Text;
import org.ejavdge.web.context.Location;
import org.ejavdge.web.context.WithEntry;
import org.ejavdge.web.driver.WebDriver;
import org.ejavdge.web.media.Cookies;
import org.ejavdge.web.resource.WebResource;
import org.ejavdge.web.spec.Request;
import org.ejavdge.web.spec.header.Header;
import org.ejavdge.web.spec.header.WithHeaders;
import org.ejavdge.web.spec.method.Get;

public final class ContestPage implements Text {
    private final Text origin;

    public ContestPage(final WebDriver d, final Location l, final Session s) {
        this(
            new Utf8Text(
                new WebResource(
                    d, l,
                    new Request(
                        new WithHeaders(
                            new Header(
                                new Text.Of("Cookie"),
                                new Cookies.ImprintOf(
                                    new WithEntry(
                                        new Text.Of("EJSID"),
                                        new Ejsid(s)
                                    )
                                )
                            ),
                            new Get(
                                new Location(
                                    new WithEntry(
                                        new Text.Of("SID"),
                                        new Sid(s)
                                    ),
                                    l
                                )
                            )
                        )
                    )
                )
            )
        );
    }

    public ContestPage(final Text origin) {
        this.origin = origin;
    }

    @Override
    public String content() throws InvariantViolation {
        return this.origin.content();
    }
}
