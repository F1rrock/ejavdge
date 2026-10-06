/**
 * Library-specific error types.
 * <p>
 * This package contains error and exception types used throughout the ejudge
 * client application. Its primary type is
 * {@link org.ejavdge.error.InvariantViolation}, an unchecked exception thrown
 * whenever an invariant of the application is violated. This includes invalid
 * inputs, unexpected states, failed operations, or any other situation where
 * the code's assumptions do not hold.
 * </p>
 * <p>
 * The exception types in this package are designed to be thrown by effects
 * (see {@link org.ejavdge.effect}) and other operations to signal that they
 * could not be completed correctly. They are unchecked, so callers are not
 * forced to handle them, but they can be caught at higher levels to provide
 * diagnostics or to recover if appropriate.
 * </p>
 */
package org.ejavdge.error;
