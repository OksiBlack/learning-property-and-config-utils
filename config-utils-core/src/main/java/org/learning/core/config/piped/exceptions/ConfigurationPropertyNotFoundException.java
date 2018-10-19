package org.learning.core.config.piped.exceptions;

public class ConfigurationMissing extends RuntimeException {

    public ConfigurationMissing(String property) {
        super(property + " not found in configuration");
    }
}
