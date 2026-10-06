/**
 * HTTP request context entries (problem, language, run id).
 * <p>
 * This package provides the abstraction for attaching named values — such as
 * credentials, session tokens, problem and language identifiers, run
 * identifiers, and location details — to web requests and other media. The
 * central type is the {@link org.ejavdge.web.context.Context} interface, a
 * functional interface whose
 * {@link org.ejavdge.web.context.Context#imprint(org.ejavdge.web.media.Media)}
 * method applies the context's data to a {@link org.ejavdge.web.media.Media},
 * producing a result of some type.
 * </p>
 * <p>
 * The package is organized around the {@code Context} interface and a set of
 * concrete implementations and combinators:
 * </p>
 * <ul>
 *   <li><b>Core abstraction and composition:</b>
 *       <ul>
 *         <li>{@link org.ejavdge.web.context.Context} – the functional interface
 *             for a web context;</li>
 *         <li>{@link org.ejavdge.web.context.WithEntry} – attaches a single
 *             named entry to a media, optionally delegating to an underlying
 *             context;</li>
 *         <li>{@link org.ejavdge.web.context.Union} – combines two contexts,
 *             applying them in sequence;</li>
 *         <li>{@link org.ejavdge.web.context.NoContext} – the neutral or
 *             identity context that adds nothing.</li>
 *       </ul>
 *   </li>
 *   <li><b>Authentication and session tokens:</b>
 *       <ul>
 *         <li>{@link org.ejavdge.web.context.Credentials} – the login, password,
 *             and contest id needed for authentication;</li>
 *         <li>{@link org.ejavdge.web.context.ContextOfEjsid} – the {@code EJSID}
 *             session cookie;</li>
 *         <li>{@link org.ejavdge.web.context.ContextOfSid} – the {@code SID}
 *             session parameter.</li>
 *       </ul>
 *   </li>
 *   <li><b>Problem and solution identifiers:</b>
 *       <ul>
 *         <li>{@link org.ejavdge.web.context.ProbId} – a problem's numeric
 *             identifier;</li>
 *         <li>{@link org.ejavdge.web.context.LangId} – a language's numeric
 *             identifier;</li>
 *         <li>{@link org.ejavdge.web.context.ContextOfSolution} – the union of
 *             a problem id and a language id, used when submitting a
 *             solution;</li>
 *         <li>{@link org.ejavdge.web.context.RunId} – the identifier of a run
 *             (submission).</li>
 *       </ul>
 *   </li>
 *   <li><b>Location:</b>
 *       <ul>
 *         <li>{@link org.ejavdge.web.context.Location} – the URL, host, and port
 *             of a resource in the contest system, with nested
 *             {@link org.ejavdge.web.context.Location.Host Location.Host} and
 *             {@link org.ejavdge.web.context.Location.Port Location.Port}
 *             contexts that expose only the host or only the port.</li>
 *       </ul>
 *   </li>
 * </ul>
 * <p>
 * Contexts are typically used by {@link org.ejavdge.web.spec.Request} and
 * related classes to construct HTTP requests, and by
 * {@link org.ejavdge.contest.ContestResource} and
 * {@link org.ejavdge.contest.ContestForm} to interact with the contest system.
 * Many context implementations wrap their textual or numeric values with
 * {@link org.ejavdge.scalar.text.TextAbout} or
 * {@link org.ejavdge.scalar.num.NumAbout} and validate them (for example, using
 * {@link org.ejavdge.scalar.num.Positive}), so that any failure while
 * materializing the values is reported with a meaningful message.
 * </p>
 */
package org.ejavdge.web.context;
