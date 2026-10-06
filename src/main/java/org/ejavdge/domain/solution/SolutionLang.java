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

/**
 * A numeric value representing the language identifier to be used when
 * submitting a solution.
 * <p>
 * The correct language for a submission can be determined in two ways:
 * <ol>
 *   <li>from the problem page itself, if the contest system has a preset
 *       language selected for that problem (see
 *       {@link org.ejavdge.domain.problem.PresetLang});</li>
 *   <li>from a marker comment inside the solution file, in which case the
 *       language is resolved by index against the language selector on the
 *       problem page (see {@link org.ejavdge.domain.problem.LangByIndex} and
 *       {@link LangIndexOf}).</li>
 * </ol>
 * This class encapsulates that logic using a {@link Fallback}: it first
 * attempts to read the preset language from the problem page, and if that is
 * not available, it falls back to the language index taken from the solution
 * file. The resulting value is labelled as "language of solution" via
 * {@link NumAbout}.
 * <p>
 * The problem page content is fetched once (via the problem's
 * {@link ProblemPage}) and reused to build both the preset-language path and
 * the index-based path, avoiding repeated network requests.
 */
public final class SolutionLang implements Num {

    /**
     * The XML engine used to evaluate XPath selections on the problem page.
     */
    private final XmlEngine engine;

    /**
     * The problem page from which the language selector is read.
     */
    private final ProblemPage problem;

    /**
     * The solution file from which the language marker is extracted as a
     * fallback.
     */
    private final ByteFile file;

    /**
     * Creates a language resolver for the given problem page and solution file.
     *
     * @param e the XML engine used to evaluate XPath selections on the problem
     *          page
     * @param p the problem page containing the language selector
     * @param f the solution file from which the language marker may be read
     */
    public SolutionLang(final XmlEngine e, final ProblemPage p, final ByteFile f) {
        this.engine = e;
        this.problem = p;
        this.file = f;
    }

    /**
     * Returns the numeric language identifier to use for the solution.
     * <p>
     * The problem page content is fetched once and reused to construct two
     * candidate values:
     * <ul>
     *   <li>the preset language of the problem, as reported by the contest
     *       system via {@link PresetLang};</li>
     *   <li>the language at the index specified by the solution file's marker
     *       comment, as resolved by {@link LangByIndex} using the index from
     *       {@link LangIndexOf}.</li>
     * </ul>
     * The preset language takes precedence; if it cannot be determined, the
     * index-based language is used. The selected value is wrapped with the
     * descriptive label "language of solution".
     *
     * @return the resolved language identifier as an integer
     * @throws InvariantViolation if neither the preset language nor the
     *         language marker in the solution file can be resolved
     */
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
