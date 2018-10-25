package org.learning.core.config.properties;

import java.util.Map;

/**
 * Created by oksana_cherniavskaia on 23.10.2018.
 */
interface ISimplePropertiesSource {


    int DEFAULT_PRIORITY =1;
    /**
     * Gets a result.
     *
     * @return a result
     */
    Map<Object, Object> getProperties();

    /**
     * Returns the order in which this IPropertiesSource has priority. A higher value means that the source will be
     * applied later so as to take precedence over other property sources.
     *
     * @return priority value
     */
    int getPriority();

}
