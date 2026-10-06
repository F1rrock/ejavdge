/**
 * Side effects and control-flow helpers.
 * <p>
 * This package provides the core abstractions for modeling side effects and
 * composing them into larger operations. Its central type is
 * {@link org.ejavdge.effect.Effect}, a functional interface whose
 * {@link org.ejavdge.effect.Effect#perform()} method executes an action that
 * may change the environment, such as sending requests, writing output, or
 * waiting for a condition.
 * </p>
 * <p>
 * The package also includes:
 * </p>
 * <ul>
 *   <li>{@link org.ejavdge.effect.Envelope} – a functional interface for
 *       sendable requests or payloads, with a
 *       {@link org.ejavdge.effect.Envelope#send()} method;</li>
 *   <li>{@link org.ejavdge.effect.SendingOf} – an adapter that turns an
 *       {@code Envelope} into an {@code Effect} by delegating
 *       {@code perform()} to {@code send()};</li>
 *   <li>{@link org.ejavdge.effect.Sequence} – a composite effect that performs
 *       a collection of effects in order, stopping at the first failure;</li>
 *   <li>{@link org.ejavdge.effect.WithDelay} – a decorator that delays the
 *       execution of another effect by a specified {@link java.time.Duration}
 *       using a {@link java.util.concurrent.ScheduledExecutorService};</li>
 *   <li>{@link org.ejavdge.effect.WithTimeout} – a decorator that runs another
 *       effect asynchronously and fails if it does not complete within a
 *       specified {@link java.time.Duration}.</li>
 * </ul>
 * <p>
 * Effects are used throughout the application as the primary means of
 * describing actions to be taken. Application entry points delegate their work
 * to an {@code Effect}, and higher-level constructs such as the
 * {@link org.ejavdge.domain.report.AwaitingOf} verdict-polling effect or the
 * {@link org.ejavdge.domain.report.AffirmingOf} guard effect are built on top
 * of this abstraction.
 * </p>
 * <p>
 * All effects may throw {@link org.ejavdge.error.InvariantViolation} if an
 * invariant is violated during execution, signaling that the effect could not
 * be completed correctly.
 * </p>
 */
package org.ejavdge.effect;
