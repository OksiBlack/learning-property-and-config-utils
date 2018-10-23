package org.learning.core.utils.properties;

import java.util.Map;
import java.util.Properties;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * Created by oksana_cherniavskaia on 18.10.2018.
 */
public interface IPropertiesSource {


    int DEFAULT_PRIORITY =1;
    /**
     * Gets a result.
     *
     * @return a result
     */
    Map<String, Object> getProperties();

    /**
     * Returns the order in which this IPropertiesSource has priority. A higher value means that the source will be
     * applied later so as to take precedence over other property sources.
     *
     * @return priority value
     */
     int getPriority();

}
