/**
 * ANSI color wrappers for terminal text.
 * <p>
 * This package provides {@link org.ejavdge.scalar.text.Text} decorators that
 * render their content in a specific color using ANSI escape codes. These
 * wrappers are useful for adding visual emphasis to textual output when it is
 * displayed in a terminal that supports ANSI colors.
 * </p>
 * <p>
 * The following color wrappers are provided:
 * </p>
 * <ul>
 *   <li>{@link org.ejavdge.scalar.text.palette.Green} – renders the wrapped
 *       text in green, typically used for success messages or positive
 *       outcomes;</li>
 *   <li>{@link org.ejavdge.scalar.text.palette.Red} – renders the wrapped text
 *       in red, typically used for errors, warnings, or negative outcomes.</li>
 * </ul>
 * <p>
 * Each wrapper is a {@code Text} implementation itself, so colored text can be
 * composed with other {@code Text} values and used anywhere a regular text is
 * expected. The underlying content is materialized lazily, and the ANSI escape
 * sequences are added only when the text is actually rendered. If the output
 * destination does not support ANSI colors, the escape sequences may appear as
 * literal characters; in such cases, these wrappers should be avoided or
 * stripped before display.
 * </p>
 */
package org.ejavdge.scalar.text.palette;
