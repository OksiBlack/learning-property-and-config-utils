package org.deprecated;

import java.util.Map;
import java.util.Properties;


/**
 * Created by oksana_cherniavskaia on 18.10.2018.
 */
public class PropertiesFileSource extends AbstractPropertiesSource {

    public PropertiesFileSource(final String fileName) {
        super(loadPropertiesFile(fileName));
    }


    private static Map<String, Object> loadPropertiesFile(final String fileName) {
        final Properties props = new Properties();


        //todo
        return null;
    }


}
