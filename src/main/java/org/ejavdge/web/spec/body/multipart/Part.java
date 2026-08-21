package org.ejavdge.web.spec.body.multipart;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;

@FunctionalInterface
public interface Part {
    byte[] content() throws InvariantViolation;

    final class Of implements Part {
        private final Bytes src;

        public Of(final Bytes bs) {
            this.src = bs;
        }

        @Override
        public byte[] content() throws InvariantViolation {
            return this.src.content();
        }
    }
}
