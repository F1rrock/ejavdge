package org.ejavdge.app.setup;

import org.ejavdge.dom.engine.JsoupWithSaxon;
import org.ejavdge.dom.engine.XmlEngine;
import org.ejavdge.dom.path.DocPath;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

public final class PresetEngine implements XmlEngine {
    private final XmlEngine origin;

    public PresetEngine() {
        this.origin = new JsoupWithSaxon();
    }

    @Override
    public String selectionOf(final Text xml, final DocPath p) throws InvariantViolation {
        return this.origin.selectionOf(xml, p);
    }
}
