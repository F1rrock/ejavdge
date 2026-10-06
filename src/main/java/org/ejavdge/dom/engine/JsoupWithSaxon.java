package org.ejavdge.dom.engine;

import net.sf.saxon.xpath.XPathFactoryImpl;
import org.ejavdge.dom.path.DocPath;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;
import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.jsoup.parser.Parser;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

/**
 * An {@link XmlEngine} implementation that parses HTML with Jsoup and evaluates
 * XPath expressions using Saxon.
 * <p>
 * This class combines the lenient HTML parsing of Jsoup with the powerful XPath
 * support of Saxon. It is used to extract string values from HTML documents by
 * evaluating XPath expressions against a DOM representation of the parsed HTML.
 * <p>
 * The HTML is parsed using Jsoup's HTML parser, then converted to a W3C DOM
 * document via {@link W3CDom}. XPath expressions are compiled and evaluated
 * using Saxon's {@link XPathFactoryImpl}.
 */
public final class JsoupWithSaxon implements XmlEngine {

    /**
     * The XPath evaluator backed by Saxon.
     */
    private final XPath xPath;

    /**
     * Creates a new engine with a Saxon XPath evaluator.
     */
    public JsoupWithSaxon() {
        this.xPath = new XPathFactoryImpl().newXPath();
    }

    /**
     * Evaluates the given XPath expression against the provided HTML text and
     * returns the resulting string.
     * <p>
     * The HTML is parsed with Jsoup using the HTML parser, converted to a W3C
     * DOM document, and then evaluated as an XPath string expression. If the
     * XPath expression is invalid, an {@link InvariantViolation} is thrown with
     * a corresponding message. If the HTML cannot be parsed or converted, an
     * {@link InvariantViolation} is also thrown.
     *
     * @param xml  the HTML document as text
     * @param path the XPath expression to evaluate
     * @return the string result of the XPath evaluation
     * @throws InvariantViolation if the XPath is invalid or the HTML cannot be
     *         processed
     */
    @Override
    public String selectionOf(final Text xml, final DocPath path) {
        try {
            return (String) this.xPath
                .compile(path.view())
                .evaluate(
                    new W3CDom().fromJsoup(
                        Jsoup.parse(
                            xml.content(),
                            "",
                            Parser.htmlParser()
                        )
                    ),
                    XPathConstants.STRING
                );
        } catch (final XPathExpressionException e) {
            throw new InvariantViolation("There is no valid XPath", e);
        } catch (final Exception e) {
            throw new InvariantViolation("There is no valid XML", e);
        }
    }
}
