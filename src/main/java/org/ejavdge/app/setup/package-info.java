/**
 * Default building blocks for running apps without explicit
 * configuration.
 *
 * <p>Two kinds of objects live here. {@code Preset*} classes are
 * ready-to-use implementations of the project's interfaces:
 * {@link org.ejavdge.app.setup.PresetDriver} wraps {@link org.ejavdge.web.driver.jdk.socket.JdkSocket} with logging,
 * {@link org.ejavdge.app.setup.PresetEngine} bundles Jsoup with Saxon, and {@link
 * org.ejavdge.app.setup.PresetOut} writes to standard output. The remaining classes are
 * configuration values read from {@code .env} at the working
 * directory — host, port, path, credentials, and contest id — each
 * with an optional preset and fallback.
 *
 * <p>An app under {@code org.ejavdge.app} uses these when it wants a
 * default pipeline without forcing the caller to construct every
 * dependency by hand. Callers that need a custom driver, engine, or
 * output bypass this package and wire the real objects directly.
 */
package org.ejavdge.app.setup;
