package org.learning.core.propsonly;

import java.util.Properties;
import org.apache.logging.log4j.util.PropertiesPropertySource;


/**
 * Created by oksana_cherniavskaia on 18.10.2018.
 */
public class PropertyFilePropertySource extends PropertiesSource {

    public PropertyFilePropertySource(final String fileName) {
        super(loadPropertiesFile(fileName));
    }

    private static Properties loadPropertiesFile(final String fileName) {
        final Properties props = new Properties();


        //todo
        return props;
    }

    @Override
    public int getPriority() {
        return 0;
    }

}
