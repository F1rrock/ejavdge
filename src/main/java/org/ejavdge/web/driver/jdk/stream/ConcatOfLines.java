package org.ejavdge.web.driver.jdk.stream;

import java.util.stream.IntStream;
import java.util.stream.Stream;

public final class ConcatOfLines implements ByteStream {
    private final char sep;
    private final Stream<int[]> src;

    public ConcatOfLines(final Stream<int[]> src) {
        this('\n', src);
    }

    public ConcatOfLines(final char sep, final Stream<int[]> src) {
        this.sep = sep;
        this.src = src;
    }

    @Override
    public IntStream content() {
        return this.src.flatMapToInt(
            s -> IntStream.concat(
                IntStream.of(s),
                IntStream.of(this.sep)
            )
        );
    }
}
