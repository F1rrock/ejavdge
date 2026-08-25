package org.ejavdge.dom;

import org.ejavdge.dom.engine.XmlEngine;
import org.ejavdge.dom.path.DocPath;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

public final class XmlSelection implements Text {
    private final XmlEngine engine;
    private final Text xml;
    private final DocPath path;

    public XmlSelection(final XmlEngine e, final Text t, final DocPath p) {
        this.engine = e;
        this.xml = t;
        this.path = p;
    }

    @Override
    public String content() throws InvariantViolation {
        return this.engine.selectionOf(this.xml, this.path);
    }
}
