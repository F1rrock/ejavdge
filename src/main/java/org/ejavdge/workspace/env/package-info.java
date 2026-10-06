/**
 * Environment variable access.
 * <p>
 * This package provides an abstraction for reading and writing environment
 * variables used to configure the workspace. Its central type is the
 * {@link org.ejavdge.workspace.env.EnvVariable} interface, which models a
 * mutable variable with a name and a current value, and offers operations for
 * reading ({@link org.ejavdge.workspace.env.EnvVariable#value()}) and
 * reassigning ({@link org.ejavdge.workspace.env.EnvVariable#assignWith}) it.
 * </p>
 * <p>
 * The package contains the following classes:
 * </p>
 * <ul>
 *   <li>{@link org.ejavdge.workspace.env.EnvVariable} – the contract for a
 *       mutable environment variable, with methods for reading the current
 *       value and assigning a new one;</li>
 *   <li>{@link org.ejavdge.workspace.env.VarOfDotenv} – an implementation of
 *       {@code EnvVariable} backed by a {@code .env} file, using the
 *       {@code dotenv-java} library for reading and rewriting the file for
 *       assignment;</li>
 *   <li>{@link org.ejavdge.workspace.env.ValueOf} – an adapter that exposes the
 *       current value of an {@code EnvVariable} as a
 *       {@link org.ejavdge.scalar.text.Text}, so it can be used wherever text
 *       is expected;</li>
 *   <li>{@link org.ejavdge.workspace.env.AssignmentWith} – an
 *       {@link org.ejavdge.effect.Effect} that assigns a value to an
 *       environment variable, allowing variable updates to be composed into
 *       effect pipelines.</li>
 * </ul>
 * <p>
 * Environment variables are typically used to parameterize the workspace — for
 * example, to specify credentials, contest identifiers, or file paths — without
 * hard-coding those values in the source. Values are represented as
 * {@link org.ejavdge.scalar.text.Text} when assigned, allowing them to be
 * computed lazily, and the {@code ValueOf} adapter allows the same values to be
 * consumed lazily wherever text is required.
 * </p>
 */
package org.ejavdge.workspace.env;
