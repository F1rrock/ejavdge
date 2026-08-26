package org.ejavdge.domain.problem;

import org.ejavdge.contest.MainPage;
import org.ejavdge.dom.XmlSelection;
import org.ejavdge.dom.engine.XmlEngine;
import org.ejavdge.dom.path.*;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.NonEmpty;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.scalar.text.TextAbout;

public final class Available implements Text {
    private final Text origin;

    public Available(final XmlEngine e, final MainPage p) {
        this(
            new TextAbout(
                "available problems",
                new NonEmpty(
                    new XmlSelection(
                        e, p,
                        new InnerText(
                            new Text.Of(", "),
                            new WithClass(
                                "tab",
                                new NestedTag(
                                    "a",
                                    new WithClass(
                                        "nTopNavList",
                                        new NestedTag(
                                            "ul",
                                            new WithId(
                                                "probNavTopList",
                                                new OnlyTag("tr")
                                            )
                                        )
                                    )
                                )
                            )
                        )
                    )
                )
            )
        );
    }

    public Available(final Text t) {
        this.origin = t;
    }

    @Override
    public String content() throws InvariantViolation {
        return this.origin.content();
    }
}
