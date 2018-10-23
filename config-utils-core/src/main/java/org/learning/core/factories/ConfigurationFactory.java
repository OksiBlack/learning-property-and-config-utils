package org.learning.core.factories;

public abstract class ConfigurationFactory {

    public abstract PropertiesConfiguration getConfiguration(ConfigurationSource source);
}