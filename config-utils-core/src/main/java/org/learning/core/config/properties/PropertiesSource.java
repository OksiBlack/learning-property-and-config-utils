package org.learning.core.config.properties;

import java.util.Properties;

/**
 * Created by oksana_cherniavskaia on 18.10.2018.
 */
public class PropertiesSource implements ISimplePropertiesSource {
    public static final int DEFAULT_PRIORITY = 1;


    public static final PropertiesSource EMPTY_SOURCE = new PropertiesSource(new Properties());
    protected Properties properties;
    protected int priority = DEFAULT_PRIORITY;


    public PropertiesSource(Properties properties) {
        this.properties = properties;
    }

    public PropertiesSource() {
        this.properties = new Properties();
    }



    public Properties getProperties() {
        return properties;
    }


    @Override
    public int getPriority() {
        return priority;
    }


    PropertiesSource from(PropertiesSource other) {
        return new PropertiesSource(new Properties(other.properties));


    }
}

