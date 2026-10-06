/**
 * Authentication and session handling for the ejudge contest system.
 * <p>
 * This package provides the classes necessary to authenticate a user against
 * an ejudge instance and to maintain an authenticated session. It includes:
 * <ul>
 *   <li>{@link org.ejavdge.auth.LoginReply} – the raw response from a login
 *       request, obtained by submitting credentials to the contest system;</li>
 *   <li>{@link org.ejavdge.auth.Session} – an authenticated session represented
 *       as raw bytes (typically cookies), which can be created by performing a
 *       login request or by wrapping existing session bytes.</li>
 * </ul>
 * The session bytes are used by other parts of the application to make
 * authenticated requests to the contest system.
 */
package org.ejavdge.auth;
