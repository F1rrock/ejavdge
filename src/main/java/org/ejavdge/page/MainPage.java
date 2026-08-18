package org.ejavdge.page;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.scalar.text.TextAbout;
import org.ejavdge.scalar.text.TextOfNum;
import org.ejavdge.scalar.text.Utf8Text;
import org.ejavdge.web.context.WithEntry;

public final class MainPage implements Text {
    private final Text origin;

    public MainPage(final ContestResource r) {
        this(
            new TextAbout(
                "main page",
                new Utf8Text(
                    new ContestResource(
                        r,
                        new WithEntry(
                            new Text.Of("amp;action"),
                            new TextOfNum(2),
                            new WithEntry(
                                new Text.Of("amp;lt"),
                                new TextOfNum(1)
                            )
                        )
                    )
                )
            )
        );
    }

    public MainPage(final Text t) {
        this.origin = t;
    }

    @Override
    public String content() throws InvariantViolation {
        return this.origin.content();
    }
}
