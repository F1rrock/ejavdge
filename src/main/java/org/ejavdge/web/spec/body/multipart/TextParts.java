package org.ejavdge.web.spec.body.multipart;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.items.Items;
import org.ejavdge.items.Joint;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.web.context.Context;
import org.ejavdge.web.media.Media;

import java.util.List;
/**
 * Text parts.
 */

public final class TextParts implements Media<List<Part>> {
    private final Items<Part> src;
    /**
     * Creates a new {@code TextParts}.
     */

    public TextParts() {
        this(new Items.Of<>());
    }
    /**
     * Creates a new {@code TextParts}.
     * @param ps the ps
     */

    public TextParts(final Items<Part> ps) {
        this.src = ps;
    }
    /**
     * Returns the result of {@code with}.
     * @param n the 'n' argument
     * @param v the 'v' argument
     * @return the s
     * @throws InvariantViolation if an invariant is violated
     */

    @Override
    public TextParts with(final Text n, final Text v) throws InvariantViolation {
        return new TextParts(
            new Joint<>(
                this.src,
                new Items.Of<>(
                    new TextPart(n, v)
                )
            )
        );
    }
    /**
     * Returns the underlying content.
     * @return the t>
     * @throws InvariantViolation if an invariant is violated
     */

    @Override
    public List<Part> content() throws InvariantViolation {
        return this.src.contents();
    }
    /**
     * ImprintOf wrapper or view over its constructor arguments.
     */

    public static final class ImprintOf implements Items<Part> {
        private final Context ctx;
        private final TextParts ps;
        /**
         * Creates a new {@code ImprintOf}.
         * @param c the 'c' argument
         */

        public ImprintOf(final Context c) {
            this.ctx = c;
            this.ps = new TextParts();
        }
        /**
         * Returns the underlying content.
         * @return the t>
         * @throws InvariantViolation if an invariant is violated
         */

        @Override
        public List<Part> contents() throws InvariantViolation {
            return this.ctx.imprint(this.ps);
        }
    }
}
