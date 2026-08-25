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

public final class JsoupWithSaxon implements XmlEngine {
    private final XPath xPath;

    public JsoupWithSaxon() {
        this.xPath = new XPathFactoryImpl().newXPath();
    }

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
