package org.ejavdge.scalar.bytes;

import org.ejavdge.error.InvariantViolation;

public interface Bytes {
    byte[] content() throws InvariantViolation;

    final class Of implements Bytes {
        private final byte[] bs;

        public Of(final byte[] bs) {
            this.bs = bs.clone();
        }

        @Override
        public byte[] content() {
            return this.bs.clone();
        }
    }
}
