package org.ejavdge.file;

import org.ejavdge.error.InvariantViolation;

public interface ByteFile {
    String name() throws InvariantViolation;
    byte[] content() throws InvariantViolation;
}
