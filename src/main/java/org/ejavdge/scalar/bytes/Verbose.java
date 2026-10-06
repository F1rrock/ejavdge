package org.ejavdge.scalar.bytes;

import org.ejavdge.scalar.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A {@link Bytes} decorator that logs a message before materializing the
 * underlying byte content.
 * <p>
 * This class wraps another {@link Bytes} instance along with a message and an
 * SLF4J {@link Logger}. When {@link #content()} is called, the message is
 * logged at the {@code DEBUG} level (if debug logging is enabled), and then the
 * content of the underlying sequence is returned unchanged.
 * <p>
 * This is useful for tracing when and how often a particular byte sequence is
 * evaluated, without affecting its behavior. Because the message only
 * evaluate when debug logging, the overhead in production
 * configurations is negligible.
 * <p>
 * If no logger provider, the logger associated with this class used by
 * default. The message can be supplied either as a plain {@link String} or as a
 * {@link Text} instance.
 */
public final class Verbose implements Bytes {

    /**
     * The underlying byte sequence whose content is being traced.
     */
    private final Bytes origin;

    /**
     * The message to log before materializing the underlying content.
     */
    private final Text message;

    /**
     * The logger used to emit the message.
     */
    private final Logger log;

    /**
     * Creates a verbose wrapper with the given byte sequence and message,
     * using the default logger.
     *
     * @param bs the underlying byte sequence
     * @param s  the message to log before materializing the content
     */
    public Verbose(final Bytes bs, final String s) {
        this(bs, new Text.Of(s));
    }

    /**
     * Creates a verbose wrapper with the given byte sequence and message,
     * using the default logger.
     *
     * @param bs the underlying byte sequence
     * @param t  the message to log before materializing the content
     */
    public Verbose(final Bytes bs, final Text t) {
        this(bs, t, LoggerFactory.getLogger(Verbose.class));
    }

    /**
     * Creates a verbose wrapper with the given byte sequence, message, and
     * logger.
     *
     * @param bs the underlying byte sequence
     * @param s  the message to log before materializing the content
     * @param l  the logger used to emit the message
     */
    public Verbose(final Bytes bs, final String s, final Logger l) {
        this(bs, new Text.Of(s), l);
    }

    /**
     * Creates a verbose wrapper with the given byte sequence, message, and
     * logger.
     *
     * @param bs the underlying byte sequence
     * @param t  the message to log before materializing the content
     * @param l  the logger used to emit the message
     */
    public Verbose(final Bytes bs, final Text t, final Logger l) {
        this.origin = bs;
        this.message = t;
        this.log = l;
    }

    /**
     * Returns the content of the underlying byte sequence, logging the
     * configured message beforehand if debug logging is enabled.
     * <p>
     * If the logger is set to debug level, the message is materialized and
     * logged at the {@code DEBUG} level. The content of the underlying byte
     * sequence is then returned unchanged.
     *
     * @return the byte content of the underlying sequence
     */
    @Override
    public byte[] content() {
        if (this.log.isDebugEnabled()) {
            this.log.debug(this.message.content());
        }
        return this.origin.content();
    }
}
