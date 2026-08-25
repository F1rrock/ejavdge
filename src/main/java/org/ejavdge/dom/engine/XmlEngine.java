package org.ejavdge.dom.engine;

import org.ejavdge.dom.path.DocPath;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

@FunctionalInterface
public interface XmlEngine {
    String selectionOf(final Text xml, final DocPath path) throws InvariantViolation;
}
