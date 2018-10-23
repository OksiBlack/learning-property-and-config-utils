package org.learning.core.utils.properties.deprecated;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.function.BiConsumer;
import org.learning.core.utils.properties.IPropertiesSource;

/**
 * Created by oksana_cherniavskaia on 22.10.2018.
 */
public abstract class AbstractPropertiesSource implements IPropertiesSource{
protected Map<String, Object> properties = new LinkedHashMap<>();
protected int priority = DEFAULT_PRIORITY;

public AbstractPropertiesSource() {

}

public AbstractPropertiesSource(Properties properties) {
    Map props = properties; //raw types
    this.properties = props;
}
/**
 * Create a Configuration decorator around the specified Map. The map is
 * used to store the configuration properties, any change will also affect
 * the Map.
 *
 * @param map the map
 */
public AbstractPropertiesSource(Map<String, ?> map)
{
    this.properties = (Map<String, Object>) map;
}

@Override
public int getPriority() {
    return priority;
}


@Override
public Map<String, Object> getProperties() {
    return properties;
}

}
