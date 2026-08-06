package org.ejavdge.web.driver.jdk.stream;

import java.util.stream.Stream;

public final class Lines {
    private final ByteStream src;
    private final char sep;

    public Lines(final ByteStream s) {
        this(s, '\n');
    }

    public Lines(final ByteStream s, char sep) {
        this.src = s;
        this.sep = sep;
    }

    public Stream<int[]> content() {
        return Stream.iterate(
            new Tee<>(this.src.content().boxed()),
            ignored -> true,
            tee -> new Tee<>(
                tee.right().dropWhile(el -> el != this.sep).skip(1)
            )
        ).map(tee -> tee.left()
            .takeWhile(el -> el != this.sep)
            .mapToInt(Integer::intValue)
            .toArray()
        );
    }
}
