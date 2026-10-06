/**
 * Session and contest tokens.
 * <p>
 * This package contains domain classes that extract and represent the session
 * and contest tokens used by the ejudge contest system. These tokens are
 * obtained from an authenticated {@link org.ejavdge.auth.Session} and are
 * required for making subsequent authenticated requests to the contest system.
 * </p>
 * <p>
 * The tokens provided by this package are:
 * </p>
 * <ul>
 *   <li>{@link org.ejavdge.domain.tokens.Ejsid} – the value of the
 *       {@code EJSID} cookie, which identifies the session on the server
 *       side;</li>
 *   <li>{@link org.ejavdge.domain.tokens.Sid} – the value of the {@code SID}
 *       query parameter, which is typically passed in URLs to identify the
 *       session.</li>
 * </ul>
 * <p>
 * Both classes implement {@link org.ejavdge.scalar.text.Text} and extract
 * their respective token from the raw bytes of a {@code Session} using
 * regular expressions. The extracted values are labelled for clarity and can
 * be used wherever a textual token is needed, such as in
 * {@link org.ejavdge.contest.ContestResource} or
 * {@link org.ejavdge.contest.ContestForm} when constructing requests.
 * </p>
 */
package org.ejavdge.domain.tokens;
