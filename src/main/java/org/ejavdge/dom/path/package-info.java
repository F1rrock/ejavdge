/**
 * Composable XPath expressions for document navigation.
 * <p>
 * This package provides a fluent, composable way to build XPath expressions
 * for selecting nodes in XML or HTML documents. The central abstraction is the
 * {@link org.ejavdge.dom.path.DocPath} interface, a functional interface whose
 * {@link org.ejavdge.dom.path.DocPath#view()} method returns the XPath
 * expression as a string. Implementations of this interface cover a wide range
 * of common selection patterns, including:
 * <ul>
 *   <li>selecting all nodes ({@link org.ejavdge.dom.path.AllNodes});</li>
 *   <li>filtering by tag name ({@link org.ejavdge.dom.path.OnlyTag},
 *       {@link org.ejavdge.dom.path.OnlyTags});</li>
 *   <li>filtering by CSS class ({@link org.ejavdge.dom.path.WithClass},
 *       {@link org.ejavdge.dom.path.WithClasses});</li>
 *   <li>filtering by id ({@link org.ejavdge.dom.path.WithId});</li>
 *   <li>filtering by name attribute ({@link org.ejavdge.dom.path.WithName},
 *       {@link org.ejavdge.dom.path.WithNames});</li>
 *   <li>filtering by exact text content ({@link org.ejavdge.dom.path.WithText});</li>
 *   <li>excluding elements by tag or class ({@link org.ejavdge.dom.path.WithoutTag},
 *       {@link org.ejavdge.dom.path.WithoutClass});</li>
 *   <li>selecting elements before or after certain siblings
 *       ({@link org.ejavdge.dom.path.BeforeTag},
 *       {@link org.ejavdge.dom.path.AfterClasses});</li>
 *   <li>selecting nested elements ({@link org.ejavdge.dom.path.NestedTag},
 *       {@link org.ejavdge.dom.path.NestedTags});</li>
 *   <li>extracting inner text, links, or attribute values
 *       ({@link org.ejavdge.dom.path.InnerText},
 *       {@link org.ejavdge.dom.path.LinksOnly},
 *       {@link org.ejavdge.dom.path.ValuesOnly});</li>
 *   <li>selecting a specific occurrence by position
 *       ({@link org.ejavdge.dom.path.OnlyAt});</li>
 *   <li>combining multiple paths ({@link org.ejavdge.dom.path.AllOf});</li>
 *   <li>binding a path to a function that produces another path
 *       ({@link org.ejavdge.dom.path.BindOfPath}).</li>
 * </ul>
 * <p>
 * In addition, the {@link org.ejavdge.dom.path.TextOfPath} adapter allows a
 * {@code DocPath} to be used wherever a
 * {@link org.ejavdge.scalar.text.Text} is expected, bridging the two
 * abstractions.
 * <p>
 * These paths are typically evaluated by an
 * {@link org.ejavdge.dom.engine.XmlEngine}, which parses a document and applies
 * the XPath expression to produce a string result.
 */
package org.ejavdge.dom.path;
