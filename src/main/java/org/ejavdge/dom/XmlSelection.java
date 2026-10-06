package org.ejavdge.dom;

import org.ejavdge.dom.engine.XmlEngine;
import org.ejavdge.dom.path.DocPath;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

/**
 * A {@link Text} that represents the result of applying an XPath expression to
 * an XML or HTML document.
 * <p>
 * This class bridges the document-selection abstractions with the text
 * abstraction: it holds an {@link XmlEngine} used to evaluate the selection, a
 * {@link Text} providing the source document, and a {@link DocPath} describing
 * the XPath expression. When {@link #content()} is called, the engine parses
 * the document and evaluates the path, returning the selected content as a
 * string.
 * <p>
 * This allows document selections to be used anywhere a {@code Text} is
 * expected, such as in string concatenation, formatting, or further processing.
 */
public final class XmlSelection implements Text {

    /**
     * The engine used to parse the document and evaluate the XPath expression.
     */
    private final XmlEngine engine;

    /**
     * The source XML or HTML document as text.
     */
    private final Text xml;

    /**
     * The XPath expression to evaluate against the document.
     */
    private final DocPath path;

    /**
     * Creates a new selection from the given engine, document, and path.
     *
     * @param e the engine that evaluates the XPath expression against the
     *          document
     * @param t the XML or HTML document to select from
     * @param p the XPath expression describing the selection
     */
    public XmlSelection(final XmlEngine e, final Text t, final DocPath p) {
        this.engine = e;
        this.xml = t;
        this.path = p;
    }

    /**
     * Returns the result of evaluating the XPath expression against the
     * document.
     * <p>
     * The evaluation is delegated to the underlying {@link XmlEngine}, which
     * parses the document and applies the path. The result is returned as a
     * string.
     *
     * @return the selected content as a string
     * @throws InvariantViolation if the document cannot be parsed, the XPath
     *         expression is invalid, or an invariant is violated during
     *         evaluation
     */
    @Override
    public String content() throws InvariantViolation {
        return this.engine.selectionOf(this.xml, this.path);
    }
}
