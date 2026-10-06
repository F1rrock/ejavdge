/**
 * File-backed program sources and downloads.
 * <p>
 * This package provides abstractions for working with named binary files and
 * executable programs. It is used throughout the application to represent
 * solution sources, problem attachments, and other files that can be read,
 * written, downloaded, or executed.
 * </p>
 * <p>
 * The central abstractions in this package are:
 * </p>
 * <ul>
 *   <li>{@link org.ejavdge.file.ByteFile} – a contract for a named file whose
 *       contents are raw bytes, with accessors for the file name and its
 *       content;</li>
 *   <li>{@link org.ejavdge.file.Program} – a functional interface for an
 *       executable program that takes textual input and produces textual
 *       output.</li>
 * </ul>
 * <p>
 * Implementations and adapters include:
 * </p>
 * <ul>
 *   <li>{@link org.ejavdge.file.JdkFile} – adapts a standard
 *       {@link java.io.File} to the {@code ByteFile} interface by reading its
 *       content from disk on demand;</li>
 *   <li>{@link org.ejavdge.file.ContentOf} – exposes the content of a
 *       {@code ByteFile} as {@link org.ejavdge.scalar.bytes.Bytes};</li>
 *   <li>{@link org.ejavdge.file.NameOf} – exposes the name of a
 *       {@code ByteFile} as {@link org.ejavdge.scalar.text.Text};</li>
 *   <li>{@link org.ejavdge.file.JavaProgram} – implements both
 *       {@code ByteFile} and {@code Program}, representing a Java source file
 *       that can be executed in single-file source mode using the
 *       {@code java} command, with its output captured;</li>
 *   <li>{@link org.ejavdge.file.DownloadingOf} – an effect that downloads a
 *       collection of remote attachments and saves them to a local
 *       directory.</li>
 * </ul>
 * <p>
 * Together, these classes allow the application to treat local files, remote
 * resources, and executable programs uniformly, bridging the gap between the
 * file system, the network, and the contest domain.
 * </p>
 */
package org.ejavdge.file;
