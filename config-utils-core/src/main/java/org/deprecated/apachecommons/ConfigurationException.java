package org.deprecated.apachecommons;

public class ConfigurationException extends RuntimeException {

/**
 * Constructs an exception with a message.
 *
 * @param message The reason for the exception
 */
public ConfigurationException(final String message) {
    super(message);
}

/**
 * Constructs an exception with a message and underlying cause.
 *
 * @param message The reason for the exception
 * @param cause   The underlying cause of the exception
 */
public ConfigurationException(final String message, final Throwable cause) {
    super(message, cause);
}

/**
 * Constructs an exception with a message and underlying cause.
 *
 * @param cause The underlying cause of the exception
 */
public ConfigurationException(final Throwable cause) {
    super(cause);
}

}