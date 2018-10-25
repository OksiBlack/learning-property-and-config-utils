package org.learning.core.config.fluent.exceptions;

public class ConfigurationPropertyNotFoundException extends RuntimeException {

    public ConfigurationPropertyNotFoundException(String property) {
        super(String.format("Configuration property %s not found. ", property));
    }
}
