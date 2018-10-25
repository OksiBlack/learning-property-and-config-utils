package org.learning.core.config.patterns.factory.factories;

public abstract class ConfigurationFactory {

    public abstract PropertiesConfiguration getConfiguration(ConfigurationSource source);
}