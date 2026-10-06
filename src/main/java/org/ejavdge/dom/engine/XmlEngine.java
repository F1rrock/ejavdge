package org.ejavdge.dom.engine;

import org.ejavdge.dom.path.DocPath;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

/**
 * A contract for evaluating XPath expressions against XML or HTML documents.
 * <p>
 * Implementations of this interface are responsible for parsing a textual
 * document and applying an XPath expression to it, returning the result as a
 * string. This allows different underlying XML/HTML processing libraries to be
 * used interchangeably.
 * <p>
 * This is a functional interface whose functional method is
 * {@link #selectionOf(Text, DocPath)}.
 */
@FunctionalInterface
public interface XmlEngine {

    /**
     * Evaluates the given XPath expression against the provided document and
     * returns the resulting string.
     *
     * @param xml  the document to evaluate the XPath against, as text
     * @param path the XPath expression to evaluate
     * @return the string result of the XPath evaluation
     * @throws InvariantViolation if the document cannot be parsed or the XPath
     *         expression is invalid or cannot be evaluated
     */
    String selectionOf(final Text xml, final DocPath path) throws InvariantViolation;
}
