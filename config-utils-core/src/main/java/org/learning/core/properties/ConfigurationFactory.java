package org.learning.core.properties;

public abstract class ConfigurationFactory {

    public abstract PropertiesConfiguration getConfiguration(ConfigurationSource source);
}