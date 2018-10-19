package org.learning.core.utils.properties;

import java.util.Properties;


/**
 * Created by oksana_cherniavskaia on 18.10.2018.
 */
public class PropertiesFileSource extends PropertiesSource {

    public PropertiesFileSource(final String fileName) {
        super(loadPropertiesFile(fileName));
    }


    private static Properties loadPropertiesFile(final String fileName) {
        final Properties props = new Properties();


        //todo
        return props;
    }


}
