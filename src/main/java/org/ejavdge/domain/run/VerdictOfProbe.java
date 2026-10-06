package org.ejavdge.domain.run;

import org.ejavdge.contest.ContestResource;
import org.ejavdge.contest.MainPage;
import org.ejavdge.contest.ProblemPage;
import org.ejavdge.dom.engine.XmlEngine;
import org.ejavdge.domain.Verdict;
import org.ejavdge.domain.problem.ProbBrief;
import org.ejavdge.domain.problem.ProbByName;
import org.ejavdge.domain.problem.ProbExamples;
import org.ejavdge.domain.solution.ProbNameOf;
import org.ejavdge.domain.solution.VerdictBySamples;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.file.JavaProgram;
import org.ejavdge.web.context.ProbId;

/**
 * A verdict that represents the outcome of probing a solution against the
 * example tests of a problem.
 * <p>
 * This class implements {@link Verdict} and is used to determine whether a
 * given Java program passes the sample tests provided in a problem statement.
 * The verdict is obtained by extracting the example fixtures from the problem
 * page and running the program against them via {@link VerdictBySamples}.
 * <p>
 * The problem is identified by the name derived from the solution file
 * ({@link ProbNameOf}), resolved to a problem ID using {@link ProbByName} on
 * the main page, and then fetched as a {@link ProblemPage}. From that page,
 * the problem brief and its examples are extracted.
 * <p>
 * An instance can also be created by wrapping an existing {@link Verdict}, in
 * which case it simply delegates the success check to that verdict.
 */
public final class VerdictOfProbe implements Verdict {

    /**
     * The underlying verdict that provides the actual result.
     */
    private final Verdict origin;

    /**
     * Creates a verdict by running the given Java program against the example
     * tests of the problem associated with the given contest resource.
     * <p>
     * The construction process is as follows:
     * <ol>
     *   <li>The problem name is derived from the solution file using
     *       {@link ProbNameOf}.</li>
     *   <li>The name is resolved to a problem ID via {@link ProbByName} on the
     *       main page.</li>
     *   <li>The problem page is fetched using that ID.</li>
     *   <li>The problem brief is extracted from the page, and its example
     *       fixtures are parsed via {@link ProbExamples}.</li>
     *   <li>A {@link VerdictBySamples} is created from the Java program and the
     *       extracted examples, and used as the underlying verdict.</li>
     * </ol>
     *
     * @param p the Java program to probe against the examples
     * @param e the XML engine used to evaluate XPath selections on contest
     *          pages
     * @param r the contest resource providing access to the contest system
     */
    public VerdictOfProbe(final JavaProgram p, final XmlEngine e, final ContestResource r) {
        this(
            new VerdictBySamples(
                p,
                new ProbExamples(
                    new ProbBrief(
                        e,
                        new ProblemPage(
                            r,
                            new ProbId(
                                new ProbByName(
                                    e,
                                    new MainPage(r),
                                    new ProbNameOf(p)
                                )
                            )
                        )
                    )
                )
            )
        );
    }

    /**
     * Creates a verdict that wraps an existing verdict.
     *
     * @param v the verdict to wrap
     */
    public VerdictOfProbe(final Verdict v) {
        this.origin = v;
    }

    /**
     * Returns whether the solution passed the example tests.
     * <p>
     * This method delegates to the underlying verdict's {@link Verdict#ok()}
     * method.
     *
     * @return {@code true} if the solution passed the example tests,
     *         {@code false} otherwise
     * @throws InvariantViolation if an invariant is violated while checking the
     *         verdict
     */
    @Override
    public boolean ok() throws InvariantViolation {
        return this.origin.ok();
    }
}
