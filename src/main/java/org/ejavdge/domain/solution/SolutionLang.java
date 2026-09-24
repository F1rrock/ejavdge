package org.ejavdge.domain.solution;

import org.ejavdge.contest.ProblemPage;
import org.ejavdge.dom.engine.XmlEngine;
import org.ejavdge.domain.problem.LangByIndex;
import org.ejavdge.domain.problem.PresetLang;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.file.ByteFile;
import org.ejavdge.scalar.num.Fallback;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.scalar.num.NumAbout;
import org.ejavdge.scalar.text.Text;

public final class SolutionLang implements Num {
    private final XmlEngine engine;
    private final ProblemPage problem;
    private final ByteFile file;

    public SolutionLang(final XmlEngine e, final ProblemPage p, final ByteFile f) {
        this.engine = e;
        this.problem = p;
        this.file = f;
    }

    @Override
    public int value() throws InvariantViolation {
        final var p = new Text.Of(this.problem.content());
        return new NumAbout(
            "language of solution",
            new Fallback(
                new PresetLang(
                    this.engine,
                    new ProblemPage(p)
                ),
                new LangByIndex(
                    this.engine,
                    new ProblemPage(p),
                    new LangIndexOf(this.file)
                )
            )
        ).value();
    }
}
