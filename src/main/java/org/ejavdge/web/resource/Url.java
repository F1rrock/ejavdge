package org.ejavdge.web.resource;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.*;
import org.ejavdge.web.context.Context;
import org.ejavdge.web.context.NoContext;
import org.ejavdge.web.context.Union;
import org.ejavdge.web.media.Form;

public final class Url implements Text {
    private final Text base;
    private final Context query;

    public Url(final Url u, final Context query) {
        this.base = u.base;
        this.query = new Union(u.query, query);
    }

    public Url(final Text t) {
        this.base = t;
        this.query = new NoContext();
    }

    @Override
    public String content() throws InvariantViolation {
        return new TextAbout(
            "url",
            new Concat(
                new TextAbout(
                    "base",
                    new NonEmpty(this.base)
                ),
                new Fallback(
                    new BindOfText(
                        new NonEmpty(
                            new Utf8Text(
                                new Form.ImprintOf(this.query)
                            )
                        ),
                        s -> new Concat(
                            new Text.Of("?"),
                            new Text.Of(s)
                        )
                    ),
                    new Empty()
                )
            )
        ).content();
    }
}
