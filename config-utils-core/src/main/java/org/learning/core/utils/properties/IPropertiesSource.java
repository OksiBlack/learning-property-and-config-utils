package org.learning.core.propsonly;

import java.util.Properties;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * Created by oksana_cherniavskaia on 18.10.2018.
 */
public interface IPropertiesSource extends Supplier<Properties> {

    /**
     * Gets a result.
     *
     * @return a result
     */
    @Override
    Properties get();

    /**
     * Returns the order in which this IPropertiesSource has priority. A higher value means that the source will be
     * applied later so as to take precedence over other property sources.
     *
     * @return priority value
     */
    int getPriority();

    /**
     * Iterates over all properties and performs an action for each key/value pair.
     *
     * @param action action to perform on each key/value pair
     */
    void forEach(BiConsumer<String, String> action);

}
