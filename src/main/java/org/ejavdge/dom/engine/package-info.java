/**
 * Engines that evaluate document selections.
 * <p>
 * This package provides abstractions for selecting content from XML or HTML
 * documents using XPath expressions. It includes:
 * <ul>
 *   <li>{@link org.ejavdge.dom.engine.XmlEngine} – a functional interface that
 *       defines the contract for evaluating an XPath expression against a
 *       document and returning the result as a string;</li>
 *   <li>{@link org.ejavdge.dom.engine.JsoupWithSaxon} – an implementation that
 *       parses HTML with Jsoup and evaluates XPath expressions using Saxon.</li>
 * </ul>
 * These engines allow different underlying parsing and XPath libraries to be
 * used interchangeably wherever document selection is required.
 */
package org.ejavdge.dom.engine;
