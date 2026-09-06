package org.ejavdge.domain.problem;

import org.ejavdge.contest.ProblemPage;
import org.ejavdge.dom.XmlSelection;
import org.ejavdge.dom.engine.XmlEngine;
import org.ejavdge.dom.path.*;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.items.Items;
import org.ejavdge.scalar.text.NonEmpty;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.scalar.text.TextAbout;
import org.ejavdge.scalar.text.Trimmed;

public final class Description implements Text {
    private final Text origin;

    public Description(final XmlEngine e, final ProblemPage p) {
        this(
            new TextAbout(
                "problem description",
                new NonEmpty(
                    new Trimmed(
                        new XmlSelection(
                            e, p,
                            new BindOfPath(
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
                                ),
                                path -> new AllOf(
                                    new InnerText(new DocPath.Of(path)),
                                    new LinksOnly(new DocPath.Of(path))
                                )
                            )
                        )
                    )
                )
            )
        );
    }

    public Description(final Text t) {
        this.origin = t;
    }

    @Override
    public String content() throws InvariantViolation {
        return this.origin.content();
    }
}
