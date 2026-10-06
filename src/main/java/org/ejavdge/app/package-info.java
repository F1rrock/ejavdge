/**
 * User-facing applications that run against an eJudge contest.
 *
 * <p>An app is the outermost layer of the project. It composes a
 * scenario from {@link org.ejavdge.app.scenario} with an output
 * channel, sends the result to that channel, and — where the
 * scenario can fail — decides whether to print a success message,
 * write an error report, or propagate the failure.
 *
 * <p>Scenarios themselves perform the work but do not print. Apps
 * are the only place in the project where the user sees anything.
 *
 * <p>Implementations are named {@code *App} to make the role
 * explicit at the call site: {@code new LocalProbeApp(...).run()}
 * reads as an app, not as a domain object that happens to have a
 * {@code run} method.
 */
package org.ejavdge.app;
