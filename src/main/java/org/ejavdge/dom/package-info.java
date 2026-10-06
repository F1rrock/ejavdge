/**
 * XML/HTML document selection utilities.
 * <p>
 * This package provides the central abstraction for selecting content from XML
 * or HTML documents and exposing the result as text. The primary class is
 * {@link org.ejavdge.dom.XmlSelection}, which implements
 * {@link org.ejavdge.scalar.text.Text} and combines an
 * {@link org.ejavdge.dom.engine.XmlEngine} (for parsing and XPath evaluation),
 * a source document as text, and a {@link org.ejavdge.dom.path.DocPath} (the
 * XPath expression).
 * <p>
 * The engines and paths used by selections are defined in the subpackages:
 * <ul>
 *   <li>{@link org.ejavdge.dom.engine} – engines that evaluate document
 *       selections;</li>
 *   <li>{@link org.ejavdge.dom.path} – composable XPath expressions for
 *       document navigation.</li>
 * </ul>
 * <p>
 * Together, these classes allow flexible and composable extraction of text
 * from HTML or XML documents, which can then be used wherever a
 * {@code Text} is expected.
 */
package org.ejavdge.dom;
