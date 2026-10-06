/**
 * Abstractions for sending HTTP requests.
 * <p>
 * This package provides the central abstraction for executing HTTP requests
 * against the contest system: the {@link org.ejavdge.web.driver.WebDriver}
 * interface. A web driver is responsible for taking a
 * {@link org.ejavdge.web.context.Location} (the target resource) and a
 * {@link org.ejavdge.web.spec.Request} (the method, headers, and body), and
 * returning the raw response bytes as a {@code byte[]}. This allows the rest of
 * the application to interact with the contest system without depending on any
 * particular HTTP client implementation.
 * </p>
 * <p>
 * The package is organized around the {@code WebDriver} interface and its
 * implementations and decorators:
 * </p>
 * <ul>
 *   <li>{@link org.ejavdge.web.driver.WebDriver} – the functional interface
 *       that defines the contract for performing a request and returning the
 *       raw response bytes;</li>
 *   <li>{@link org.ejavdge.web.driver.WithLogsDriver} – a decorator that wraps
 *       another driver and logs the outgoing request bytes at trace level
 *       before delegating, useful for debugging and auditing the exact requests
 *       sent to the contest system.</li>
 * </ul>
 * <p>
 * The primary implementation of {@code WebDriver} lives in the subpackage
 * {@link org.ejavdge.web.driver.jdk.socket}, which uses plain JDK sockets to
 * send requests and parse responses. That package provides a low-level HTTP
 * client with full control over the wire format, and it handles response body
 * decoding (both {@code Content-Length}-bounded and chunked transfer-encoded)
 * through the policies in
 * {@link org.ejavdge.web.driver.jdk.socket.body}.
 * </p>
 * <p>
 * Web drivers are used throughout the application wherever a resource needs to
 * be fetched from the contest system. For example:
 * </p>
 * <ul>
 *   <li>{@link org.ejavdge.contest.ContestResource} uses a driver to retrieve
 *       contest pages and other resources;</li>
 *   <li>{@link org.ejavdge.auth.LoginReply} uses a driver to perform the login
 *       request;</li>
 *   <li>{@link org.ejavdge.web.resource.WebResource} constructs higher-level
 *       requests that are ultimately executed by a driver.</li>
 * </ul>
 * <p>
 * By abstracting away the transport mechanism behind the {@code WebDriver}
 * interface, the application can be tested with mock drivers, instrumented with
 * logging or retrying decorators, or switched to a different HTTP client
 * implementation without affecting the rest of the code.
 * </p>
 */
package org.ejavdge.web.driver;
