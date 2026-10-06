/**
 * User-facing operations on eJudge contests, expressed as effects.
 *
 * <p>A scenario performs one complete action against the contest —
 * download attachments, submit a solution, wait for the report —
 * but does not print anything. Output and success messages are the
 * caller's concern: apps in {@code org.ejavdge.app} compose a
 * scenario with an {@code Out} and decide what the user sees.
 *
 * <p>Scenarios are built from the primitives in {@code
 * org.ejavdge.effect} ({@code Sequence}, {@code WithTimeout}) and
 * from domain objects in {@code org.ejavdge.domain}. They are the
 * only place in the project where those pieces are wired into a
 * complete operation.
 */
package org.ejavdge.app.scenario;
