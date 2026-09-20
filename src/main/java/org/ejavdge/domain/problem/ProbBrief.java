package org.ejavdge.domain.problem;

import org.ejavdge.contest.ProblemPage;
import org.ejavdge.dom.XmlSelection;
import org.ejavdge.dom.engine.XmlEngine;
import org.ejavdge.dom.path.*;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.items.Items;
import org.ejavdge.items.Lines;
import org.ejavdge.items.Map;
import org.ejavdge.scalar.text.*;

public final class ProbBrief implements Text {
    private final Text origin;

    public ProbBrief(final XmlEngine e, final ProblemPage p) {
        this(
            new TextAbout(
                "problem description",
                new NonEmpty(
                    new Concat(
                        new Text.Of("\n"),
                        new Map<>(
                            Trimmed::new,
                            new Lines(
                                new WithoutNbsp(
                                    new XmlSelection(
                                        e, p,
                                        new InnerText(
                                            new WithoutClass(
                                                "line-table-wb",
                                                new WithoutTags(
                                                    new Items.Of<>(
                                                        new Text.Of("br"),
                                                        new Text.Of("style")
                                                    ),
                                                    new BeforeId(
                                                        "ej-submit-tabs",
                                                        new ChildrenOf(
                                                            new WithId(
                                                                "probNavTaskArea-ins",
                                                                new OnlyTag("div")
                                                            )
                                                        )
                                                    )
                                                )
                                            )
                                        )
                                    )
                                )
                            )
                        )
                    ),
                    new Text.Of("There is no such problem.")
                )
            )
        );
    }

    public ProbBrief(final Text t) {
        this.origin = t;
    }

    @Override
    public String content() throws InvariantViolation {
        return this.origin.content();
    }
}
