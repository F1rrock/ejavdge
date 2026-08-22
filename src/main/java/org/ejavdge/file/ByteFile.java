package org.ejavdge.file;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.text.Text;

public interface ByteFile {
    String name() throws InvariantViolation;
    byte[] content() throws InvariantViolation;

    final class Of implements ByteFile {
        private final Text name;
        private final Bytes value;

        public Of(final Text n, final Bytes bs) {
            this.name = n;
            this.value = bs;
        }

        @Override
        public String name() throws InvariantViolation {
            return this.name.content();
        }

        @Override
        public byte[] content() throws InvariantViolation {
            return this.value.content();
        }
    }
}
