package org.ejavdge.domain.problem;

import org.ejavdge.contest.ProblemPage;
import org.ejavdge.dom.XmlSelection;
import org.ejavdge.dom.engine.XmlEngine;
import org.ejavdge.dom.path.*;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.scalar.num.NumAbout;
import org.ejavdge.scalar.num.NumOfText;
import org.ejavdge.scalar.text.NonEmpty;

public final class PresetLang implements Num {
    private final Num origin;

    public PresetLang(final XmlEngine e, final ProblemPage p) {
        this(
            new NumAbout(
                "problem's preset language",
                new NumOfText(
                    new NonEmpty(
                        new XmlSelection(
                            e, p,
                            new ValuesOnly(
                                new WithName(
                                    "lang_id",
                                    new NestedTag(
                                        "input",
                                        new WithClass(
                                            "b0",
                                            new NestedTag(
                                                "table",
                                                new NestedTag(
                                                    "form",
                                                    new WithId(
                                                        "ej-submit-tabs",
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
            )
        );
    }

    public PresetLang(final Num n) {
        this.origin = n;
    }

    @Override
    public int value() throws InvariantViolation {
        return this.origin.value();
    }
}
