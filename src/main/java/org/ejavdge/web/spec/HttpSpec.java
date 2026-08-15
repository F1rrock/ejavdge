package org.ejavdge.web.spec;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;

@FunctionalInterface
public interface HttpSpec {
    byte[] bytes() throws InvariantViolation;

    final class Of implements HttpSpec {
        final Bytes src;

        public Of(final byte[] src) {
            this(new Bytes.Of(src));
        }

        public Of(final Bytes src) {
            this.src = src;
        }

        @Override
        public byte[] bytes() {
            return this.src.content();
        }
    }
}
