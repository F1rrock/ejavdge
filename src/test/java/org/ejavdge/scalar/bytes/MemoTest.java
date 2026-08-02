package org.ejavdge.scalar.bytes;

import junit.framework.TestCase;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;

public final class MemoTest extends TestCase {
    public void testContent() {
        assertEquals(
            "hello",
            new String(
                new Memo(
                    new Bytes.Of(
                        "hello".getBytes(StandardCharsets.UTF_8)
                    )
                ).content(),
                StandardCharsets.UTF_8
            )
        );
    }

    public void testContentIsCopied() {
        final var memo = new Memo(
            new Bytes.Of(
                "hello".getBytes(StandardCharsets.UTF_8)
            )
        );
        final byte[] first = memo.content();
        first[0] = 'X';
        assertEquals(
            "hello",
            new String(
                memo.content(),
                StandardCharsets.UTF_8
            )
        );
    }

    public void testOriginIsEvaluatedOnce() {
        final var calls = new AtomicInteger();
        final Bytes origin = () -> {
            calls.incrementAndGet();
            return "hello".getBytes(StandardCharsets.UTF_8);
        };
        final var memo = new Memo(origin);
        memo.content();
        memo.content();
        assertEquals(1, calls.get());
    }

    public void testEmptyIsEvaluatedOnce() {
        final var calls = new AtomicInteger();
        final Bytes origin = () -> {
            calls.incrementAndGet();
            return new byte[0];
        };
        final var memo = new Memo(origin);
        memo.content();
        memo.content();
        assertEquals(1, calls.get());
    }
}
