package org.learning.core.propsonly;

import java.util.Map;
import java.util.Properties;
import java.util.function.BiConsumer;

/**
 * Created by oksana_cherniavskaia on 18.10.2018.
 */
public class PropertiesSource implements IPropertiesSource {

    private final Properties properties;

    public PropertiesSource(Properties properties) {
        this.properties = properties;
    }


    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public void forEach(final BiConsumer<String, String> action) {
        for (final Map.Entry<Object, Object> entry : properties.entrySet()) {
            action.accept(((String) entry.getKey()), ((String) entry.getValue()));
        }
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
}
