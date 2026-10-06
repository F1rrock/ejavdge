package org.ejavdge.app.setup;

import org.ejavdge.dom.engine.JsoupWithSaxon;
import org.ejavdge.dom.engine.XmlEngine;
import org.ejavdge.dom.path.DocPath;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

/**
 * The default {@link XmlEngine} used by apps that do not need a
 * custom parser.
 *
 * <p>Delegates to {@link JsoupWithSaxon}: Jsoup parses the HTML into
 * a DOM, Saxon evaluates XPath expressions against it. This is the
 * engine wired into domain objects like {@code ProbBrief} and {@code
 * ProbAttachments} when the caller does not pass one explicitly.
 *
 * <p>Tests that need to control the parsed document, or apps that
 * need a different parser, bypass this class and pass their own
 * {@code XmlEngine} to the domain constructors.
 */
public final class PresetEngine implements XmlEngine {
    private final XmlEngine origin;

    /**
     * Uses {@link JsoupWithSaxon} as the underlying engine.
     */
    public PresetEngine() {
        this.origin = new JsoupWithSaxon();
    }

    @Override
    public String selectionOf(final Text xml, final DocPath p) throws InvariantViolation {
        return this.origin.selectionOf(xml, p);
    }
}
