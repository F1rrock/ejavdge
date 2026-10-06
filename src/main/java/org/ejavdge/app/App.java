package org.ejavdge.app;

import org.ejavdge.error.InvariantViolation;

/**
 * A user-facing operation that can be run.
 *
 * <p>An app is the outermost layer of the project. It composes a
 * scenario from {@code org.ejavdge.app.scenario} with an output
 * channel and decides what the user sees: success messages, error
 * reports, formatted results. Scenarios themselves do not print —
 * only apps do.
 *
 * <p>Implementations are not required to be idempotent. Running the
 * same app twice may perform the underlying operation twice; whether
 * that is meaningful depends on the scenario.
 *
 * <p>Because this is a functional interface, apps can be written as
 * lambdas when a full class is not warranted.
 */
@FunctionalInterface
public interface App {

    /**
     * Runs this application.
     *
     * @throws InvariantViolation if the underlying operation fails,
     *     or if any input it relies on is invalid
     */
    void run() throws InvariantViolation;
}
