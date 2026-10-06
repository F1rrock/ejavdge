/**
 * JDK socket implementation of the web driver.
 * <p>
 * This package provides the lowest-level HTTP client in the application: a
 * {@link org.ejavdge.web.driver.WebDriver} implementation that communicates
 * with remote servers using plain JDK sockets. It is responsible for opening
 * TCP connections, sending raw HTTP requests, and reading and parsing the
 * responses. Because it operates directly on the wire format, it offers full
 * control over the request and response bytes, which is useful for interacting
 * with contest systems that may not fully conform to higher-level HTTP
 * abstractions.
 * </p>
 * <p>
 * The entry point of the package is
 * {@link org.ejavdge.web.driver.jdk.socket.JdkSocket}, which implements the
 * {@link org.ejavdge.web.driver.WebDriver} interface. Given a
 * {@link org.ejavdge.web.context.Location} and a
 * {@link org.ejavdge.web.spec.Request}, it performs the full request-response
 * cycle and returns the response as a single byte array containing the header
 * block, a terminator, and the decoded body.
 * </p>
 * <p>
 * The package is organized around the following classes:
 * </p>
 * <ul>
 *   <li>{@link org.ejavdge.web.driver.jdk.socket.JdkSocket} – the web driver
 *       implementation itself, orchestrating the request, response parsing,
 *       and body decoding;</li>
 *   <li>{@link org.ejavdge.web.driver.jdk.socket.Reply} – a single
 *       request-reply cycle over a socket, responsible for opening the
 *       connection, sending the request bytes, and returning the response
 *       input stream;</li>
 *   <li>{@link org.ejavdge.web.driver.jdk.socket.Inet} – a network endpoint
 *       (host and port) that knows how to open a socket connection to it;</li>
 *   <li>{@link org.ejavdge.web.driver.jdk.socket.HttpResponse} – a parsed HTTP
 *       response that separates the header block from the body and exposes
 *       them as separate streams;</li>
 *   <li>{@link org.ejavdge.web.driver.jdk.socket.HeadersOf} – a
 *       {@link org.ejavdge.scalar.bytes.Bytes} view over the raw header bytes
 *       of an HTTP response;</li>
 *   <li>{@link org.ejavdge.web.driver.jdk.socket.BytesOfReply} – a
 *       {@link org.ejavdge.web.driver.jdk.stream.ByteStream} view over the raw
 *       bytes read from a socket reply.</li>
 * </ul>
 * <p>
 * Response body decoding is handled by the
 * {@link org.ejavdge.web.driver.jdk.socket.body} subpackage, which contains
 * policies for reading bodies bounded by {@code Content-Length} or encoded
 * with chunked transfer encoding, as well as a fallback for unsupported
 * structures. These policies are applied automatically by {@code JdkSocket}
 * based on the response headers.
 * </p>
 * <p>
 * Errors encountered while opening sockets, sending requests, reading
 * responses, or decoding bodies are reported as
 * {@link org.ejavdge.error.InvariantViolation} exceptions with descriptive
 * messages, so that failures at the network level are surfaced consistently
 * with the rest of the application.
 * </p>
 */
package org.ejavdge.web.driver.jdk.socket;
