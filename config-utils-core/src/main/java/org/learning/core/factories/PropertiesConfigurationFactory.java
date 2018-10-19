package org.learning.core.properties;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


/**
 * Created by oksana_cherniavskaia on 18.10.2018.
 */
public class PropertiesConfigurationFactory extends ConfigurationFactory {



    @Override
    public PropertiesConfiguration getConfiguration( final ConfigurationSource source) {
        final Properties properties = new Properties();
        try (final InputStream configStream = source.getInputStream()) {
            properties.load(configStream);
        } catch (final IOException ioe) {
            throw new ConfigurationException("Unable to load " + source.toString(), ioe);
        }
        return new PropertiesConfigurationBuilder()
            .setConfigurationSource(source)
            .build();
    }
}

