package org.learning.core.properties;

import java.util.Map;
import java.util.Properties;
import java.util.function.BiConsumer;
import org.learning.core.utils.properties.IPropertiesSource;

/**
 * Created by oksana_cherniavskaia on 18.10.2018.
 */
public class PropertiesSource implements IPropertiesSource {

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
        return get();
    }

    /**
     * Gets a result.
     *
     * @return a result
     */
    @Override
    public Properties get() {
        return properties;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public void forEach(final BiConsumer<String, String> action) {
        for (final Map.Entry<Object, Object> entry : properties.entrySet()) {
            action.accept(((String) entry.getKey()), ((String) entry.getValue()));
        }
    }


    PropertiesSource from(PropertiesSource other) {
        return new PropertiesSource(new Properties(other.properties));


    }
}