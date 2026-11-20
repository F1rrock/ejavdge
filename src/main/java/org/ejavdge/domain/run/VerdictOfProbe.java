package org.ejavdge.domain.run;

import org.ejavdge.contest.ContestResource;
import org.ejavdge.contest.MainPage;
import org.ejavdge.contest.ProblemPage;
import org.ejavdge.dom.engine.XmlEngine;
import org.ejavdge.domain.Verdict;
import org.ejavdge.domain.problem.*;
import org.ejavdge.domain.solution.SourceFromTemplate;
import org.ejavdge.domain.solution.TemplateOfSolution;
import org.ejavdge.domain.solution.ProbNameOf;
import org.ejavdge.domain.solution.VerdictBySamples;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.file.*;
import org.ejavdge.scalar.bytes.Memo;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.web.context.ProbId;

/**
 * A verdict that represents the outcome of probing a solution against the
 * example tests of a problem.
 * <p>
 * This class implements {@link Verdict} and is used to determine whether a
 * given Java program passes the sample tests provided in a problem statement.
 * The verdict is obtained by extracting the example fixtures from the problem
 * page and running the program, with the problem's optional Java header and
 * footer, against them via {@link VerdictBySamples}.
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
     * Creates a deferred verdict for the given Java program and contest.
     * The problem lookup, source preparation, and execution take place when
     * {@link #ok()} is called.
     *
     * @param p the Java program to probe against the examples
     * @param e the XML engine used to evaluate XPath selections on contest
     *          pages
     * @param r the contest resource providing access to the contest system
     */
    public VerdictOfProbe(final JavaProgram p, final XmlEngine e, final ContestResource r) {
        this(new VerdictOfProbe.AgainstProblemSamples(p, e, r));
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

    /**
     * A verdict that probes a Java program against the examples of its problem.
     * <p>
     * Each check resolves the problem ID once and uses it both to obtain the
     * examples and to prepare the source with {@link TemplateOfSolution}. The
     * assembled source is shared by all examples evaluated in that check. The
     * original file and working directory are unchanged.
     */
    public static final class AgainstProblemSamples implements Verdict {
        /**
         * The original program to probe.
         */
        private final JavaProgram program;
        /**
         * The XML engine used to read the contest pages.
         */
        private final XmlEngine engine;
        /**
         * The authenticated contest resource.
         */
        private final ContestResource resource;

        /**
         * Creates a probe for the given program and contest.
         *
         * @param p the original Java program
         * @param e the XML engine used to read the main page and problem statement
         * @param r the authenticated contest resource
         */
        public AgainstProblemSamples(final JavaProgram p, final XmlEngine e, final ContestResource r) {
            this.program = p;
            this.engine = e;
            this.resource = r;
        }

        /**
         * Checks the program against the problem's examples.
         * Source assembled for this check is discarded after the check completes.
         *
         * @return whether the program's output matches every example
         * @throws InvariantViolation if the problem cannot be resolved, the examples
         *         or source cannot be prepared, or the program cannot be run
         */
        @Override
        public boolean ok() throws InvariantViolation {
            final var name = new ProbNameOf(this.program).content();
            final var id = new ProbByName(
                this.engine,
                new MainPage(this.resource),
                new Text.Of(name)
            ).value();
            return new VerdictBySamples(
                new JavaProgram.WithSource(
                    this.program,
                    new Memo(
                        new SourceFromTemplate(
                            new TemplateOfSolution(id, name, this.resource),
                            new ContentOf(this.program)
                        )
                    )
                ),
                new ProbExamples(
                    new ProbBrief(
                        this.engine,
                        new ProblemPage(
                            this.resource,
                            new ProbId(id)
                        )
                    )
                )
            ).ok();
        }
    }
}
