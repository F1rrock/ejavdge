/**
 * Parsed HTTP resources and URL components.
 * <p>
 * This package provides utilities for extracting and representing components of
 * HTTP resources, such as URLs, response headers, and status codes. It serves
 * as the bridge between the low-level transport layer (the web driver) and the
 * higher-level contest domain, allowing the rest of the application to work
 * with parsed resources as {@link org.ejavdge.scalar.bytes.Bytes} or
 * {@link org.ejavdge.scalar.text.Text} values.
 * </p>
 * <p>
 * The main classes in this package can be grouped into the following
 * categories:
 * </p>
 * <ul>
 *   <li><b>HTTP response inspection:</b>
 *       <ul>
 *         <li>{@link org.ejavdge.web.resource.ContentLength} – extracts the
 *             numeric value of the {@code Content-Length} header;</li>
 *         <li>{@link org.ejavdge.web.resource.TransferEncoding} – extracts the
 *             value of the {@code Transfer-Encoding} header;</li>
 *         <li>{@link org.ejavdge.web.resource.Status} – extracts the HTTP
 *             status code from the status line of a response;</li>
 *         <li>{@link org.ejavdge.web.resource.HasStatus} – a decorator that
 *             verifies the response status matches an expected value;</li>
 *         <li>{@link org.ejavdge.web.resource.PayloadOf} – extracts the body
 *             (payload) from a full HTTP response.</li>
 *       </ul>
 *   </li>
 *   <li><b>URL parsing and composition:</b>
 *       <ul>
 *         <li>{@link org.ejavdge.web.resource.Url} – represents a URL with an
 *             optional query string built from a context;</li>
 *         <li>{@link org.ejavdge.web.resource.HostOf} – extracts the host
 *             component from a URL;</li>
 *         <li>{@link org.ejavdge.web.resource.PortOf} – extracts the port from
 *             a URL, applying the default port for the scheme if absent;</li>
 *         <li>{@link org.ejavdge.web.resource.PathOf} – extracts the path
 *             component from a URL;</li>
 *         <li>{@link org.ejavdge.web.resource.LastSegmentOf} – extracts the
 *             final segment of a URL path, such as a file name.</li>
 *       </ul>
 *   </li>
 *   <li><b>Bridge to the web driver:</b>
 *       <ul>
 *         <li>{@link org.ejavdge.web.resource.WebResource} – a
 *             {@link org.ejavdge.scalar.bytes.Bytes} that fetches a resource by
 *             delegating to a {@link org.ejavdge.web.driver.WebDriver}, binding
 *             together a driver, a location, and a request.</li>
 *       </ul>
 *   </li>
 * </ul>
 * <p>
 * These classes are used throughout the application to inspect HTTP responses
 * (for example, to decide how to decode the body or to verify that a login was
 * successful), to construct and manipulate URLs (for example, to append query
 * parameters when requesting contest pages), and to perform requests in a
 * uniform way. Error conditions, such as missing headers or malformed URLs, are
 * reported as {@link org.ejavdge.error.InvariantViolation} exceptions with
 * descriptive messages.
 * </p>
 */
package org.ejavdge.web.resource;
