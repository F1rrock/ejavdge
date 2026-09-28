package org.ejavdge.domain.run;

import org.ejavdge.contest.ProblemPage;
import org.ejavdge.dom.XmlSelection;
import org.ejavdge.dom.engine.XmlEngine;
import org.ejavdge.dom.path.*;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.scalar.text.NonEmpty;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.scalar.text.TextAbout;
import org.ejavdge.scalar.text.Trimmed;

public final class LastRunId implements Text {
    private final Text origin;

    public LastRunId(final XmlEngine e, final ProblemPage p) {
        this(
            new TextAbout(
                "last run id",
                new NonEmpty(
                    new Trimmed(
                        new XmlSelection(
                            e, p,
                            new InnerText(
                                new OnlyAt(
                                    new Num.Of(1),
                                    new WithClass(
                                        "b1",
                                        new NestedTag(
                                            "td",
                                            new OnlyAt(
                                                new Num.Of(2),
                                                new PathToTableRows()
                                            )
                                        )
                                    )
                                )
                            )
                        )
                    ),
                    new Text.Of("There is no reports here.")
                )
            )
        );
    }

    public LastRunId(final Text t) {
        this.origin = t;
    }

    @Override
    public String content() throws InvariantViolation {
        return this.origin.content();
    }
}
