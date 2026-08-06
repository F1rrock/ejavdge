package org.ejavdge.web.driver.jdk.stream;

import java.util.stream.IntStream;

public interface ByteStream {
    IntStream content();

    final class Of implements ByteStream {
        private final IntStream src;

        public Of(final IntStream rs) {
            this.src = rs;
        }

        public IntStream content() {
            return this.src;
        }
    }
}
