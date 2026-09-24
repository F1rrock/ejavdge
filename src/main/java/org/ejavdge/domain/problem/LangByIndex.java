package org.ejavdge.domain.problem;

import org.ejavdge.contest.ProblemPage;
import org.ejavdge.dom.XmlSelection;
import org.ejavdge.dom.engine.XmlEngine;
import org.ejavdge.dom.path.*;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.num.*;

public final class LangByIndex implements Num {
    private final Num origin;

    public LangByIndex(final XmlEngine e, final ProblemPage p, final Num n) {
        this(
            new NumAbout(
                "problem's language id",
                new NumOfText(
                    new XmlSelection(
                        e, p,
                        new ValuesOnly(
                            new OnlyAt(
                                new SumOf(
                                    new Positive(n),
                                    new Num.Of(1)
                                ),
                                new ChildrenOf(
                                    new WithName(
                                        "lang_id",
                                        new OnlyTag("select")
                                    )
                                )
                            )
                        )
                    )
                )
            )
        );
    }

    public LangByIndex(final Num n) {
        this.origin = n;
    }

    @Override
    public int value() throws InvariantViolation {
        return this.origin.value();
    }
}
