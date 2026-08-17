package org.ejavdge.scalar.bytes;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

public final class WithTimeoutTest extends TestCase {
    public void testOriginalBytes() {
        assertEquals(
            "HelloWorld",
            new String(
                new WithTimeout(
                    new Bytes.Of(
                        "HelloWorld".getBytes(StandardCharsets.UTF_8)
                    ),
                    Duration.ofSeconds(1)
                ).content(),
                StandardCharsets.UTF_8
            )
        );
    }

    public void testTimeout() {
        try {
            new WithTimeout(
                new Bytes.Of(new byte[0]),
                Duration.ofSeconds(1),
                () -> new ExecutorService() {
                    @Override
                    public <T> Future<T> submit(Callable<T> task) {
                        return CompletableFuture.failedFuture(new TimeoutException());
                    }
                    @Override
                    public <T> Future<T> submit(Runnable task, T result) {
                        return CompletableFuture.failedFuture(new TimeoutException());
                    }
                    @Override
                    public Future<?> submit(Runnable task) {
                        return CompletableFuture.failedFuture(new TimeoutException());
                    }
                    @Override public void shutdown() {throw new UnsupportedOperationException(); }
                    @Override public List<Runnable> shutdownNow() { return List.of(); }
                    @Override public boolean isShutdown() { return false; }
                    @Override public boolean isTerminated() { return false; }
                    @Override public boolean awaitTermination(long timeout, TimeUnit unit) { return false; }
                    @Override public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) { return List.of(); }
                    @Override public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) { return List.of(); }
                    @Override public <T> T invokeAny(Collection<? extends Callable<T>> tasks) { throw new UnsupportedOperationException(); }
                    @Override public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) { throw new UnsupportedOperationException(); }
                    @Override public void execute(Runnable command) { throw new UnsupportedOperationException(); }
                }
            ).content();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testBrokenOrigin() {
        try {
            new WithTimeout(
                () -> {
                    throw new InvariantViolation("original");
                },
                Duration.ofSeconds(1)
            ).content();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }
}
