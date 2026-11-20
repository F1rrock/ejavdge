package org.ejavdge.domain.solution;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;

/**
 * Java source bytes produced by applying a {@link TemplateOfSolution} to a
 * solution.
 * <p>
 * The template is applied when {@link #content()} is called. The original
 * solution is left unchanged.
 */
public final class SourceFromTemplate implements Bytes {
    /**
     * The template to apply to the solution.
     */
    private final TemplateOfSolution template;
    /**
     * The original solution source.
     */
    private final Bytes origin;

    /**
     * Creates a deferred source from a template and a solution.
     *
     * @param t the template providing the header and footer
     * @param bs the original solution source
     */
    public SourceFromTemplate(final TemplateOfSolution t, final Bytes bs) {
        this.template = t;
        this.origin = bs;
    }

    /**
     * Returns the source assembled from the template and the solution.
     *
     * @return the header, solution source, and footer concatenated as bytes
     * @throws InvariantViolation if any part of the source cannot be obtained
     */
    @Override
    public byte[] content() throws InvariantViolation {
        return this.template.applyTo(this.origin);
    }
}
