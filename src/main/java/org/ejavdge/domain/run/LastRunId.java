package org.ejavdge.domain.run;

import org.ejavdge.contest.ProblemPage;
import org.ejavdge.dom.XmlSelection;
import org.ejavdge.dom.engine.XmlEngine;
import org.ejavdge.dom.path.*;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.scalar.num.NumAbout;
import org.ejavdge.scalar.num.NumOfText;
import org.ejavdge.scalar.text.NonEmpty;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.scalar.text.Trimmed;

public final class LastRunId implements Num {
    private final Num origin;

    public LastRunId(final XmlEngine e, final ProblemPage p) {
        this(
            new NumAbout(
                "last run id",
                new NumOfText(
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
            )
        );
    }

    public LastRunId(final Num n) {
        this.origin = n;
    }

    @Override
    public int value() throws InvariantViolation {
        return this.origin.value();
    }
}
